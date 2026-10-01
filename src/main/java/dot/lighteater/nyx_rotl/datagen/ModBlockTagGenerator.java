package dot.lighteater.nyx_rotl.datagen;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.utility.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {

    public ModBlockTagGenerator(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper) {

        super(output, lookupProvider, NyxROTL.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.METEOR_ROCK.get())
                .add(ModBlocks.GLEANING_METEOR_ROCK.get())
                .add(ModBlocks.METEOR_BLOCK.get())
                .add(ModBlocks.STAR_BLOCK.get())
                .add(ModBlocks.STAR_STAIRS.get())
                .add(ModBlocks.STAR_SLAB.get())
                .add(ModBlocks.STAR_FENCE.get())
                .add(ModBlocks.STAR_FENCE_GATE.get())
                .add(ModBlocks.STAR_WALL.get())
                .add(ModBlocks.CHISELED_STAR_BLOCK.get())
                .add(ModBlocks.CHISELED_STAR_STAIRS.get())
                .add(ModBlocks.CHISELED_STAR_SLAB.get())
                .add(ModBlocks.CHISELED_STAR_FENCE.get())
                .add(ModBlocks.CHISELED_STAR_FENCE_GATE.get())
                .add(ModBlocks.CHISELED_STAR_WALL.get())
                .add(ModBlocks.CRACKED_STAR_BLOCK.get())
                .add(ModBlocks.CRACKED_STAR_STAIRS.get())
                .add(ModBlocks.CRACKED_STAR_SLAB.get())
                .add(ModBlocks.CRACKED_STAR_FENCE.get())
                .add(ModBlocks.CRACKED_STAR_FENCE_GATE.get())
                .add(ModBlocks.CRACKED_STAR_WALL.get())
                .add(ModBlocks.CRYSTAL.get())
                .add(ModBlocks.METEOR_GLASS.get())
                .add(ModBlocks.LUNAR_WATER.get())
                .add(ModBlocks.LUNAR_WATER_CAULDRON.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.METEOR_ROCK.get())
                .add(ModBlocks.GLEANING_METEOR_ROCK.get())
                .add(ModBlocks.METEOR_BLOCK.get())
                .add(ModBlocks.METEOR_GLASS.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.CRYSTAL.get())
                .add(ModBlocks.STAR_BLOCK.get())
                .add(ModBlocks.STAR_STAIRS.get())
                .add(ModBlocks.STAR_SLAB.get())
                .add(ModBlocks.STAR_FENCE.get())
                .add(ModBlocks.STAR_FENCE_GATE.get())
                .add(ModBlocks.STAR_WALL.get())
                .add(ModBlocks.CHISELED_STAR_BLOCK.get())
                .add(ModBlocks.CHISELED_STAR_STAIRS.get())
                .add(ModBlocks.CHISELED_STAR_SLAB.get())
                .add(ModBlocks.CHISELED_STAR_FENCE.get())
                .add(ModBlocks.CHISELED_STAR_FENCE_GATE.get())
                .add(ModBlocks.CHISELED_STAR_WALL.get())
                .add(ModBlocks.CRACKED_STAR_BLOCK.get())
                .add(ModBlocks.CRACKED_STAR_STAIRS.get())
                .add(ModBlocks.CRACKED_STAR_SLAB.get())
                .add(ModBlocks.CRACKED_STAR_FENCE.get())
                .add(ModBlocks.CRACKED_STAR_FENCE_GATE.get())
                .add(ModBlocks.CRACKED_STAR_WALL.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL);

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL);

        this.tag(ModTags.Blocks.NEEDS_METEOR_TOOL);

        this.tag(BlockTags.FENCES)
                .add(ModBlocks.STAR_FENCE.get())
                .add(ModBlocks.CHISELED_STAR_FENCE.get())
                .add(ModBlocks.CRACKED_STAR_FENCE.get());

        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.STAR_FENCE_GATE.get())
                .add(ModBlocks.CHISELED_STAR_FENCE_GATE.get())
                .add(ModBlocks.CRACKED_STAR_FENCE_GATE.get());

        this.tag(BlockTags.WALLS)
                .add(ModBlocks.STAR_WALL.get())
                .add(ModBlocks.CHISELED_STAR_WALL.get())
                .add(ModBlocks.CRACKED_STAR_WALL.get());
    }
}