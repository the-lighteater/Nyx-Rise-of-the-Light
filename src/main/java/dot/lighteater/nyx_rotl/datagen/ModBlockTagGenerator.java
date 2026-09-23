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
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NyxROTL.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE);

        this.tag(BlockTags.NEEDS_IRON_TOOL);

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

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