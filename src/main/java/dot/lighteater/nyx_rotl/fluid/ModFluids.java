package dot.lighteater.nyx_rotl.fluid;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, NyxROTL.MODID);
    public static final RegistryObject<FlowingFluid> LUNAR_WATER = REGISTRY.register("lunar_water", LunarWaterFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_LUNAR_WATER = REGISTRY.register("flowing_lunar_water", LunarWaterFluid.Flowing::new);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class FluidsClientSideHandler {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(LUNAR_WATER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FLOWING_LUNAR_WATER.get(), RenderType.translucent());
        }
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}