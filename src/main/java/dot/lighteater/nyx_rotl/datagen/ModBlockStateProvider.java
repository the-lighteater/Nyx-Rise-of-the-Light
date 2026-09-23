package dot.lighteater.nyx_rotl.datagen;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, NyxROTL.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.METEOR_GLASS);
        blockWithItem(ModBlocks.METEOR_ROCK);
        blockWithItem(ModBlocks.GLEANING_METEOR_ROCK);

        blockWithItem(ModBlocks.METEOR_BLOCK);

        blockWithItem(ModBlocks.STAR_BLOCK);
        blockWithItem(ModBlocks.CHISELED_STAR_BLOCK);
        blockWithItem(ModBlocks.CRACKED_STAR_BLOCK);

        stairsBlock(((StairBlock) ModBlocks.STAR_STAIRS.get()), blockTexture(ModBlocks.STAR_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.STAR_SLAB.get()), blockTexture(ModBlocks.STAR_BLOCK.get()), blockTexture(ModBlocks.STAR_BLOCK.get()));

        fenceBlock(((FenceBlock) ModBlocks.STAR_FENCE.get()), blockTexture(ModBlocks.STAR_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.STAR_FENCE_GATE.get()), blockTexture(ModBlocks.STAR_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.STAR_WALL.get()), blockTexture(ModBlocks.STAR_BLOCK.get()));

        stairsBlock(((StairBlock) ModBlocks.CHISELED_STAR_STAIRS.get()), blockTexture(ModBlocks.CHISELED_STAR_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.CHISELED_STAR_SLAB.get()), blockTexture(ModBlocks.CHISELED_STAR_BLOCK.get()), blockTexture(ModBlocks.CHISELED_STAR_BLOCK.get()));

        fenceBlock(((FenceBlock) ModBlocks.CHISELED_STAR_FENCE.get()), blockTexture(ModBlocks.CHISELED_STAR_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.CHISELED_STAR_FENCE_GATE.get()), blockTexture(ModBlocks.CHISELED_STAR_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.CHISELED_STAR_WALL.get()), blockTexture(ModBlocks.CHISELED_STAR_BLOCK.get()));

        stairsBlock(((StairBlock) ModBlocks.CRACKED_STAR_STAIRS.get()), blockTexture(ModBlocks.CRACKED_STAR_BLOCK.get()));
        slabBlock(((SlabBlock) ModBlocks.CRACKED_STAR_SLAB.get()), blockTexture(ModBlocks.CRACKED_STAR_BLOCK.get()), blockTexture(ModBlocks.CRACKED_STAR_BLOCK.get()));

        fenceBlock(((FenceBlock) ModBlocks.CRACKED_STAR_FENCE.get()), blockTexture(ModBlocks.CRACKED_STAR_BLOCK.get()));
        fenceGateBlock(((FenceGateBlock) ModBlocks.CRACKED_STAR_FENCE_GATE.get()), blockTexture(ModBlocks.CRACKED_STAR_BLOCK.get()));
        wallBlock(((WallBlock) ModBlocks.CRACKED_STAR_WALL.get()), blockTexture(ModBlocks.CRACKED_STAR_BLOCK.get()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
