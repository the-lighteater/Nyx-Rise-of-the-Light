package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, NyxROTL.MODID);

    public static final RegistryObject<Block> CRYSTAL = registerBlock("crystal",
            () -> new CrystalBlock(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).sound(SoundType.AMETHYST).strength(3f).lightLevel(s -> 15).noOcclusion().randomTicks()));

    public static final RegistryObject<Block> METEOR_GLASS = registerBlock("meteor_glass",
            MeteorGlassBlock::new);

    public static final RegistryObject<Block> LUNAR_WATER = registerBlock("lunar_water",
            LunarWaterBlock::new);

    public static final RegistryObject<Block> LUNAR_WATER_CAULDRON = registerBlock(
            "lunar_water_cauldron",
            LunarWaterCauldron::new
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
