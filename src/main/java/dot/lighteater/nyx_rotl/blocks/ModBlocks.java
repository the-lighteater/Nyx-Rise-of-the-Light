package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
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

    public static final RegistryObject<Block> METEOR_ROCK = registerBlock("meteor_rock",
            MeteorRock::new);

    public static final RegistryObject<Block> GLEANING_METEOR_ROCK = registerBlock("gleaning_meteor_rock",
            MeteorRock::new);

    public static final RegistryObject<Block> METEOR_BLOCK = registerBlock("meteor_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final RegistryObject<Block> STAR_BLOCK = registerBlock("star_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> STAR_STAIRS = registerBlock("star_stairs",
            () -> new StairBlock(() -> ModBlocks.STAR_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> STAR_SLAB = registerBlock("star_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> STAR_FENCE = registerBlock("star_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> STAR_FENCE_GATE = registerBlock("star_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST), SoundEvents.CHAIN_PLACE, SoundEvents.ANVIL_BREAK));
    public static final RegistryObject<Block> STAR_WALL = registerBlock("star_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> CHISELED_STAR_BLOCK = registerBlock("chiseled_star_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> CHISELED_STAR_STAIRS = registerBlock("chiseled_star_stairs",
            () -> new StairBlock(() -> ModBlocks.CHISELED_STAR_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> CHISELED_STAR_SLAB = registerBlock("chiseled_star_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> CHISELED_STAR_FENCE = registerBlock("chiseled_star_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> CHISELED_STAR_FENCE_GATE = registerBlock("chiseled_star_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST), SoundEvents.CHAIN_PLACE, SoundEvents.ANVIL_BREAK));
    public static final RegistryObject<Block> CHISELED_STAR_WALL = registerBlock("chiseled_star_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> CRACKED_STAR_BLOCK = registerBlock("cracked_star_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> CRACKED_STAR_STAIRS = registerBlock("cracked_star_stairs",
            () -> new StairBlock(() -> ModBlocks.CRACKED_STAR_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> CRACKED_STAR_SLAB = registerBlock("cracked_star_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> CRACKED_STAR_FENCE = registerBlock("cracked_star_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> CRACKED_STAR_FENCE_GATE = registerBlock("cracked_star_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST), SoundEvents.CHAIN_PLACE, SoundEvents.ANVIL_BREAK));
    public static final RegistryObject<Block> CRACKED_STAR_WALL = registerBlock("cracked_star_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

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
