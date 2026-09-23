package dot.lighteater.nyx_rotl.item;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.enchantments.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
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

                for (RegistryObject<Block> block : ModBlocks.BLOCKS.getEntries()) {
                    output.accept(block.get());
                }

                for (RegistryObject<Item> item : ModItems.ITEMS.getEntries()) {
                    output.accept(item.get());
                }

                // Lunar Edge I-V
                for (int level = 1; level <= 5; level++) {
                    ItemStack book = EnchantedBookItem.createForEnchantment(
                            new EnchantmentInstance(
                                    ModEnchantments.LUNAR_EDGE.get(),
                                    level
                            )
                    );

                    output.accept(book);
                }

                // Lunar Shield I-IV
                for (int level = 1; level <= 4; level++) {
                    ItemStack book = EnchantedBookItem.createForEnchantment(
                            new EnchantmentInstance(
                                    ModEnchantments.LUNAR_SHIELD.get(),
                                    level
                            )
                    );

                    output.accept(book);
                }

            }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
