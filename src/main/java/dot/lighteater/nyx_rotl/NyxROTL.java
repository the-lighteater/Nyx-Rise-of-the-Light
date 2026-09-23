package dot.lighteater.nyx_rotl;

import com.mojang.logging.LogUtils;
import dot.lighteater.nyx_rotl.blocks.ModBlockEntities;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.enchantments.ModEnchantments;
import dot.lighteater.nyx_rotl.entities.EmptyRenderer;
import dot.lighteater.nyx_rotl.entities.MeteorKatRenderer;
import dot.lighteater.nyx_rotl.entities.MeteorRenderer;
import dot.lighteater.nyx_rotl.event.ModItemProperties;
import dot.lighteater.nyx_rotl.fluid.ModFluidTypes;
import dot.lighteater.nyx_rotl.fluid.ModFluids;
import dot.lighteater.nyx_rotl.item.ModCreativeModTabs;
import dot.lighteater.nyx_rotl.item.ModItems;
import dot.lighteater.nyx_rotl.network.PacketHandler;
import dot.lighteater.nyx_rotl.registry.ModEntities;
import dot.lighteater.nyx_rotl.registry.ModSounds;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// Naming conventions and file management is credited to Kaupenjoe and his excellent 1.20.1 Forge Modding Tutorials.
// The link to his work will be credited in the description on CurseForge

@Mod(NyxROTL.MODID)
public class NyxROTL
{

    public static final String MODID = "nyx_rotl";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NyxROTL(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);

        ModSounds.register(modEventBus);

        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);

        ModEnchantments.register(modEventBus);

        PacketHandler.init();

        ModEntities.register(modEventBus);

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::onCommonSetup);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("[ModSounds] Registered sounds:");

            ModSounds.SOUND_EVENTS.getEntries().forEach(sound -> {
                LOGGER.info(" - {}", sound.getId());
            });
        });
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.FALLING_STAR.get(), EmptyRenderer::new);
            EntityRenderers.register(ModEntities.FALLING_METEOR.get(), MeteorRenderer::new);
            EntityRenderers.register(ModEntities.METEOR_KAT.get(), MeteorKatRenderer::new);
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.CRYSTAL.get(),
                    RenderType.translucent()
            );
            ItemBlockRenderTypes.setRenderLayer(
                    ModBlocks.METEOR_GLASS.get(),
                    RenderType.translucent()
            );

            ModItemProperties.register();
        }
    }
}
