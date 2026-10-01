package dot.lighteater.nyx_rotl.item.custom;

import dot.lighteater.nyx_rotl.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class GleaningScythe extends SwordItem {

    private static final int RANGE = 4;

    private static final float HARVEST_SPEED = 12.0F;

    public GleaningScythe(Tier tier, Properties properties) {
        super(
                tier,
                10,
                -2.8F,
                properties.stacksTo(1)
        );
    }

    @Override
    public boolean canApplyAtEnchantingTable(
            ItemStack stack,
            Enchantment enchantment
    ) {
        return super.canApplyAtEnchantingTable(stack, enchantment)
                || enchantment.category == EnchantmentCategory.DIGGER;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        Block block = state.getBlock();

        return block instanceof IPlantable
                || state.is(BlockTags.LEAVES);
    }

    @Override
    public float getDestroySpeed(
            ItemStack stack,
            BlockState state
    ) {
        if (isHarvestable(state)) {
            return HARVEST_SPEED;
        }

        return super.getDestroySpeed(stack, state);
    }

    private boolean isHarvestable(BlockState state) {
        Block block = state.getBlock();

        return block instanceof IPlantable
                || state.is(BlockTags.LEAVES);
    }

    @Override
    public boolean onBlockStartBreak(
            ItemStack stack,
            BlockPos pos,
            Player player
    ) {
        Level level = player.level();

        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }

        BlockState state = serverLevel.getBlockState(pos);
        Block block = state.getBlock();

        // Only mass-break plants and leaves.
        if (!isHarvestable(state)) {
            return false;
        }

        // Don't harvest an immature crop.
        if (block instanceof CropBlock crop
                && !crop.isMaxAge(state)) {
            return false;
        }

        for (int x = -RANGE; x <= RANGE; x++) {
            for (int y = -RANGE; y <= RANGE; y++) {
                for (int z = -RANGE; z <= RANGE; z++) {

                    BlockPos target = pos.offset(x, y, z);
                    BlockState targetState =
                            serverLevel.getBlockState(target);

                    Block targetBlock = targetState.getBlock();

                    // Only plants/leaves.
                    if (!isHarvestable(targetState)) {
                        continue;
                    }

                    // Don't duplicate immature crops.
                    if (targetBlock instanceof CropBlock crop
                            && !crop.isMaxAge(targetState)) {
                        continue;
                    }

                    harvestBlock(
                            stack,
                            serverLevel,
                            target,
                            targetState,
                            player
                    );
                }
            }
        }

        return true;
    }

    private void harvestBlock(
            ItemStack scythe,
            ServerLevel level,
            BlockPos pos,
            BlockState state,
            Player player
    ) {
        List<ItemStack> drops = Block.getDrops(
                state,
                level,
                pos,
                level.getBlockEntity(pos),
                player,
                scythe
        );

        for (ItemStack drop : drops) {
            if (isBlacklisted(drop)) {
                Block.popResource(level, pos, drop);
                continue;
            }

            applyDropMultiplier(drop, level);

            Block.popResource(level, pos, drop);
        }

        level.setBlock(
                pos,
                Blocks.AIR.defaultBlockState(),
                3
        );

        scythe.hurtAndBreak(
                1,
                player,
                p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND)
        );
    }

    private boolean isBlacklisted(ItemStack drop) {
        if (drop.isEmpty()) {
            return false;
        }

        ResourceLocation itemId =
                ForgeRegistries.ITEMS.getKey(drop.getItem());

        if (itemId == null) {
            return false;
        }

        return Config.SCYTHE_DROP_BLACKLIST
                .get()
                .contains(itemId.toString());
    }

    private void applyDropMultiplier(
            ItemStack drop,
            Level level
    ) {
        for (String entry : Config.SCYTHE_DROP_CHANCES.get()) {
            String[] split = entry.split(";");

            if (split.length != 2) {
                continue;
            }

            double chance;
            int multiplier;

            try {
                chance = Double.parseDouble(split[0]);
                multiplier = Integer.parseInt(split[1]);
            } catch (NumberFormatException ignored) {
                continue;
            }

            if (level.random.nextDouble() <= chance) {
                int newAmount = Math.min(
                        drop.getMaxStackSize(),
                        drop.getCount() * multiplier
                );

                drop.setCount(newAmount);
                break;
            }
        }
    }
}