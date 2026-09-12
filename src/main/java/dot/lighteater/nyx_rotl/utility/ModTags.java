package dot.lighteater.nyx_rotl.utility;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_METEOR_TOOL = tag("needs_meteor_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(NyxROTL.MODID, name));
        }
    }

    public static class Items {

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(NyxROTL.MODID, name));
        }
    }
}