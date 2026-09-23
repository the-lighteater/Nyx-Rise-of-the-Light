package dot.lighteater.nyx_rotl.datagen;


import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.CRYSTAL.get());
        this.dropSelf(ModBlocks.METEOR_GLASS.get());

        this.dropSelf(ModBlocks.LUNAR_WATER.get());

        this.dropOther(ModBlocks.LUNAR_WATER_CAULDRON.get(), Blocks.CAULDRON);

        this.dropOther(ModBlocks.METEOR_ROCK.get(), ModItems.METEOR_SHARD.get());

        this.dropOther(ModBlocks.GLEANING_METEOR_ROCK.get(), ModItems.UNREFINED_CRYSTAL.get());

        this.dropSelf(ModBlocks.METEOR_BLOCK.get());

        this.dropSelf(ModBlocks.STAR_BLOCK.get());
        this.dropSelf(ModBlocks.STAR_STAIRS.get());
        this.dropSelf(ModBlocks.STAR_SLAB.get());
        this.dropSelf(ModBlocks.STAR_FENCE.get());
        this.dropSelf(ModBlocks.STAR_FENCE_GATE.get());
        this.dropSelf(ModBlocks.STAR_WALL.get());

        this.dropSelf(ModBlocks.CHISELED_STAR_BLOCK.get());
        this.dropSelf(ModBlocks.CHISELED_STAR_STAIRS.get());
        this.dropSelf(ModBlocks.CHISELED_STAR_SLAB.get());
        this.dropSelf(ModBlocks.CHISELED_STAR_FENCE.get());
        this.dropSelf(ModBlocks.CHISELED_STAR_FENCE_GATE.get());
        this.dropSelf(ModBlocks.CHISELED_STAR_WALL.get());

        this.dropSelf(ModBlocks.CRACKED_STAR_BLOCK.get());
        this.dropSelf(ModBlocks.CRACKED_STAR_STAIRS.get());
        this.dropSelf(ModBlocks.CRACKED_STAR_SLAB.get());
        this.dropSelf(ModBlocks.CRACKED_STAR_FENCE.get());
        this.dropSelf(ModBlocks.CRACKED_STAR_FENCE_GATE.get());
        this.dropSelf(ModBlocks.CRACKED_STAR_WALL.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}