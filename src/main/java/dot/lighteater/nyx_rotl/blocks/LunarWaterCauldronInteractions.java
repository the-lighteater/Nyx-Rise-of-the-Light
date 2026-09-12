package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class LunarWaterCauldronInteractions {

    public static final Map<Item, CauldronInteraction> INTERACTIONS =
            CauldronInteraction.newInteractionMap();

    static {
        NyxROTL.LOGGER.debug(
                "[LunarWaterCauldronInteractions] Registering cauldron interactions"
        );

        INTERACTIONS.put(
                Items.GLASS_BOTTLE,
                LunarWaterCauldronInteractions::fillBottle
        );

        INTERACTIONS.put(
                Items.BUCKET,
                LunarWaterCauldronInteractions::fillBucket
        );

        NyxROTL.LOGGER.debug(
                "[LunarWaterCauldronInteractions] Registered GLASS_BOTTLE interaction: {}",
                INTERACTIONS.containsKey(Items.GLASS_BOTTLE)
        );

        NyxROTL.LOGGER.debug(
                "[LunarWaterCauldronInteractions] Registered BUCKET interaction: {}",
                INTERACTIONS.containsKey(Items.BUCKET)
        );
    }

    private static InteractionResult fillBottle(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            net.minecraft.world.InteractionHand hand,
            ItemStack stack
    ) {
        int waterLevel = state.getValue(LayeredCauldronBlock.LEVEL);

        NyxROTL.LOGGER.debug(
                "[LunarWaterCauldronInteractions] fillBottle called | " +
                        "pos={} | level={} | client={} | player={} | hand={} | stack={} | count={}",
                pos,
                waterLevel,
                level.isClientSide(),
                player.getName().getString(),
                hand,
                stack.getItem(),
                stack.getCount()
        );

        if (waterLevel <= 0) {
            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] fillBottle FAILED: water level <= 0"
            );

            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] fillBottle SERVER: creating Lunar Water Bottle"
            );

            ItemStack bottle =
                    new ItemStack(ModItems.LUNAR_WATER_BOTTLE.get());

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] Created bottle: {} | count={}",
                    bottle.getItem(),
                    bottle.getCount()
            );

            if (!player.getAbilities().instabuild) {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Player is not creative - shrinking glass bottle"
                );

                stack.shrink(1);

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Glass bottle after shrink: " +
                                "empty={} | count={}",
                        stack.isEmpty(),
                        stack.getCount()
                );

            } else {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Player is creative - glass bottle will not be consumed"
                );
            }

            if (stack.isEmpty()) {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Original glass bottle is empty - replacing held item with Lunar Water Bottle"
                );

                player.setItemInHand(hand, bottle);

            } else {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Original stack still exists - attempting to add Lunar Water Bottle to inventory"
                );

                boolean added = player.getInventory().add(bottle);

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Inventory add result: {}",
                        added
                );

                if (!added) {

                    NyxROTL.LOGGER.debug(
                            "[LunarWaterCauldronInteractions] Inventory full - dropping Lunar Water Bottle"
                    );

                    player.drop(bottle, false);
                }
            }

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] Lowering cauldron level | oldLevel={}",
                    waterLevel
            );

            LayeredCauldronBlock.lowerFillLevel(state, level, pos);

            BlockState resultingState = level.getBlockState(pos);

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] Cauldron state after lowering: {} | level={}",
                    resultingState.getBlock(),
                    resultingState.hasProperty(LayeredCauldronBlock.LEVEL)
                            ? resultingState.getValue(LayeredCauldronBlock.LEVEL)
                            : -1
            );

            level.playSound(
                    null,
                    pos,
                    SoundEvents.BOTTLE_FILL,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] fillBottle COMPLETE"
            );
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static InteractionResult fillBucket(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            net.minecraft.world.InteractionHand hand,
            ItemStack stack
    ) {
        int waterLevel = state.getValue(LayeredCauldronBlock.LEVEL);

        NyxROTL.LOGGER.debug(
                "[LunarWaterCauldronInteractions] fillBucket called | " +
                        "pos={} | level={} | client={} | player={} | hand={} | stack={} | count={}",
                pos,
                waterLevel,
                level.isClientSide(),
                player.getName().getString(),
                hand,
                stack.getItem(),
                stack.getCount()
        );

        if (waterLevel != 3) {

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] fillBucket FAILED: cauldron is not full | level={}",
                    waterLevel
            );

            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] fillBucket SERVER: creating Lunar Water Bucket"
            );

            ItemStack bucket =
                    new ItemStack(ModItems.LUNAR_WATER_BUCKET.get());

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] Created bucket: {} | count={}",
                    bucket.getItem(),
                    bucket.getCount()
            );

            if (!player.getAbilities().instabuild) {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Player is not creative - shrinking empty bucket"
                );

                stack.shrink(1);

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Empty bucket after shrink: " +
                                "empty={} | count={}",
                        stack.isEmpty(),
                        stack.getCount()
                );

            } else {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Player is creative - empty bucket will not be consumed"
                );
            }

            if (stack.isEmpty()) {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Original bucket is empty - replacing held item with Lunar Water Bucket"
                );

                player.setItemInHand(hand, bucket);

            } else {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Original bucket still exists - attempting to add Lunar Water Bucket to inventory"
                );

                boolean added = player.getInventory().add(bucket);

                NyxROTL.LOGGER.debug(
                        "[LunarWaterCauldronInteractions] Inventory add result: {}",
                        added
                );

                if (!added) {

                    NyxROTL.LOGGER.debug(
                            "[LunarWaterCauldronInteractions] Inventory full - dropping Lunar Water Bucket"
                    );

                    player.drop(bucket, false);
                }
            }

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] Replacing Lunar Water Cauldron with vanilla empty cauldron"
            );

            level.setBlockAndUpdate(
                    pos,
                    Blocks.CAULDRON.defaultBlockState()
            );

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] Cauldron state after bucket operation: {}",
                    level.getBlockState(pos)
            );

            level.playSound(
                    null,
                    pos,
                    SoundEvents.BUCKET_FILL,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );

            NyxROTL.LOGGER.debug(
                    "[LunarWaterCauldronInteractions] fillBucket COMPLETE"
            );
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}