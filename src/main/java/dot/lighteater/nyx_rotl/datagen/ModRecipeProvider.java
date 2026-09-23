package dot.lighteater.nyx_rotl.datagen;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> METEOR_SMELTABLES = List.of(ModItems.METEOR_SHARD.get(),
            ModBlocks.METEOR_ROCK.get(), ModBlocks.GLEANING_METEOR_ROCK.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, METEOR_SMELTABLES, RecipeCategory.MISC, ModItems.METEOR_INGOT.get(), 0.25f, 200, "meteor");
        oreBlasting(pWriter, METEOR_SMELTABLES, RecipeCategory.MISC, ModItems.METEOR_INGOT.get(), 0.25f, 100, "meteor");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.METEOR_BLOCK.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModItems.METEOR_INGOT.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_HELMET.get())
                .pattern("MMM")
                .pattern("M M")
                .define('M', ModItems.METEOR_INGOT.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_CHESTPLATE.get())
                .pattern("M M")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModItems.METEOR_INGOT.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_LEGGINGS.get())
                .pattern("MMM")
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.METEOR_INGOT.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_BOOTS.get())
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.METEOR_INGOT.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_SWORD.get())
                .pattern("M")
                .pattern("M")
                .pattern("S")
                .define('M', ModItems.METEOR_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_PICKAXE.get())
                .pattern("MMM")
                .pattern(" S ")
                .pattern(" S ")
                .define('M', ModItems.METEOR_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_HOE.get())
                .pattern("MM")
                .pattern(" S")
                .pattern(" S")
                .define('M', ModItems.METEOR_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_SHOVEL.get())
                .pattern("M")
                .pattern("S")
                .pattern("S")
                .define('M', ModItems.METEOR_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_AXE.get())
                .pattern("MM")
                .pattern("MS")
                .pattern(" S")
                .define('M', ModItems.METEOR_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METEOR_HAMMER.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern(" S ")
                .define('M', ModItems.METEOR_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SCYTHE.get())
                .pattern("MMM")
                .pattern("M S")
                .pattern("  S")
                .define('M', ModItems.METEOR_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STAR_SLAB.get())
                .pattern("SSS")
                .define('S', ModBlocks.STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STAR_STAIRS.get())
                .pattern("S  ")
                .pattern("SS ")
                .pattern("SSS")
                .define('S', ModBlocks.STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STAR_WALL.get())
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModBlocks.STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STAR_FENCE.get())
                .pattern("SFS")
                .define('S', ModBlocks.STAR_BLOCK.get())
                .define('F', ModItems.FALLEN_STAR.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STAR_FENCE_GATE.get())
                .pattern("FSF")
                .define('S', ModBlocks.STAR_BLOCK.get())
                .define('F', ModItems.FALLEN_STAR.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRACKED_STAR_SLAB.get())
                .pattern("SSS")
                .define('S', ModBlocks.CRACKED_STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRACKED_STAR_STAIRS.get())
                .pattern("S  ")
                .pattern("SS ")
                .pattern("SSS")
                .define('S', ModBlocks.CRACKED_STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRACKED_STAR_WALL.get())
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModBlocks.CRACKED_STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRACKED_STAR_FENCE.get())
                .pattern("SFS")
                .define('S', ModBlocks.CRACKED_STAR_BLOCK.get())
                .define('F', ModItems.FALLEN_STAR.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CRACKED_STAR_FENCE_GATE.get())
                .pattern("FSF")
                .define('S', ModBlocks.CRACKED_STAR_BLOCK.get())
                .define('F', ModItems.FALLEN_STAR.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHISELED_STAR_SLAB.get())
                .pattern("SSS")
                .define('S', ModBlocks.CHISELED_STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHISELED_STAR_STAIRS.get())
                .pattern("S  ")
                .pattern("SS ")
                .pattern("SSS")
                .define('S', ModBlocks.CHISELED_STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHISELED_STAR_WALL.get())
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModBlocks.CHISELED_STAR_BLOCK.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHISELED_STAR_FENCE.get())
                .pattern("SFS")
                .define('S', ModBlocks.CHISELED_STAR_BLOCK.get())
                .define('F', ModItems.FALLEN_STAR.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHISELED_STAR_FENCE_GATE.get())
                .pattern("FSF")
                .define('S', ModBlocks.CHISELED_STAR_BLOCK.get())
                .define('F', ModItems.FALLEN_STAR.get())
                .unlockedBy(getHasName(ModItems.METEOR_INGOT.get()), has(ModItems.METEOR_INGOT.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.METEOR_INGOT.get(), 9)
                .requires(ModBlocks.METEOR_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.METEOR_BLOCK.get()), has(ModBlocks.METEOR_BLOCK.get()))
                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer,  NyxROTL.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}