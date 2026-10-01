package dot.lighteater.nyx_rotl.item;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.utility.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier METEOR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 1500, 4f, 4f, 25,
                    ModTags.Blocks.NEEDS_METEOR_TOOL, () -> Ingredient.of(ModItems.METEOR_INGOT.get())),
            new ResourceLocation(NyxROTL.MODID, "meteor"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier GLEANING = TierSortingRegistry.registerTier(
            new ForgeTier(5,3450, 4.0F, 0.0F, 25,
                    ModTags.Blocks.NEEDS_GLEANING_TOOL, () -> Ingredient.of(ModItems.UNREFINED_CRYSTAL.get())),
            new ResourceLocation(NyxROTL.MODID, "gleaning"), List.of(Tiers.DIAMOND), List.of()
    );
}