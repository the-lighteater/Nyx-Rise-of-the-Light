package dot.lighteater.nyx_rotl.item;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NyxROTL.MODID);

    public static final RegistryObject<CreativeModeTab> UPGRADE_SCROLLS = CREATIVE_MODE_TABS.register("upgrade_scrolls", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(ModItems.METEOR_AXE.get())) // Testing
            .title(Component.translatable("creativetab.nyx_rotl"))
            .displayItems((parameters, output) -> {

                output.accept(ModItems.METEOR_AXE.get());

                output.accept(ModItems.FALLEN_STAR.get());

                output.accept(ModItems.LUNAR_WATER_BUCKET.get());

                output.accept(ModItems.LUNAR_WATER_BOTTLE.get());

                output.accept(ModBlocks.LUNAR_WATER_CAULDRON.get());
                output.accept(ModBlocks.METEOR_GLASS.get());

            }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
