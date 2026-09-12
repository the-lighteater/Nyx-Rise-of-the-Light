package dot.lighteater.nyx_rotl.event;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.LunarWaterConversion;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Events handled by the mod.

@Mod.EventBusSubscriber(modid = NyxROTL.MODID)
public class ModEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        Level level = event.getLevel();

        if (level.isClientSide)
            return;

        NyxWorld nyx = NyxWorld.get((ServerLevel) level);
        if (nyx == null)
            return;

//        // sync world data to players
//        if (entity instanceof ServerPlayer player) {
//            PacketNyxWorld packet = new PacketNyxWorld(nyx);
//            PacketHandler.sendTo(player, packet);
//        }
//
//        // inject wolf AI
//        if (entity instanceof Wolf wolf) {
//
//            // avoid double-adding task (important!)
//            if (!hasMoonTask(wolf)) {
//                wolf.targetSelector.addGoal(3, new WolfAISpecialMoon(wolf));
//            }
//        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        Player player = event.getEntity();
        Level level = player.level();

        // Only care about the main hand.
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

        // Only care about the Lunar Water Bucket.
        if (!stack.is(ModItems.LUNAR_WATER_BUCKET.get())) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // Only fill vanilla empty cauldrons.
        if (state.getBlock() != Blocks.CAULDRON) {
            return;
        }

        NyxROTL.LOGGER.debug(
                "[LunarWaterBucketInteraction] Empty vanilla cauldron found | pos={} | client={} | player={}",
                pos,
                level.isClientSide(),
                player.getName().getString()
        );

        if (!level.isClientSide()) {

            BlockState newState =
                    ModBlocks.LUNAR_WATER_CAULDRON.get()
                            .defaultBlockState()
                            .setValue(
                                    LayeredCauldronBlock.LEVEL,
                                    3
                            );

            level.setBlockAndUpdate(pos, newState);

            NyxROTL.LOGGER.debug(
                    "[LunarWaterBucketInteraction] Filled cauldron with Lunar Water | state={}",
                    newState
            );

            // Survival: consume Lunar Water Bucket and return empty bucket.
            if (!player.getAbilities().instabuild) {

                stack.shrink(1);

                ItemStack emptyBucket = new ItemStack(Items.BUCKET);

                if (stack.isEmpty()) {
                    player.setItemInHand(
                            InteractionHand.MAIN_HAND,
                            emptyBucket
                    );
                } else if (!player.getInventory().add(emptyBucket)) {
                    player.drop(emptyBucket, false);
                }

                NyxROTL.LOGGER.debug(
                        "[LunarWaterBucketInteraction] Consumed Lunar Water Bucket"
                );

            } else {

                NyxROTL.LOGGER.debug(
                        "[LunarWaterBucketInteraction] Player is creative - bucket not consumed"
                );
            }

            level.playSound(
                    null,
                    pos,
                    net.minecraft.sounds.SoundEvents.BUCKET_EMPTY,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );
        }

        // Tell Minecraft the interaction was handled.
        event.setCancellationResult(
                InteractionResult.sidedSuccess(level.isClientSide())
        );
        event.setCanceled(true);
    }


    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!(event.level instanceof ServerLevel level)) {
            return;
        }

        LunarWaterConversion.tick(level);

        for (BlockPos pos : LunarWaterConversion.getIncensedCauldrons(level)) {

            BlockState state = level.getBlockState(pos);

            if (state.getBlock() != Blocks.WATER_CAULDRON) {
                continue;
            }

            if (!LunarWaterConversion.isIncensed(level, pos)) {
                continue;
            }

            AABB searchArea = new AABB(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    pos.getX() + 1,
                    pos.getY() + 2,
                    pos.getZ() + 1
            );

            for (ItemEntity itemEntity : level.getEntitiesOfClass(
                    ItemEntity.class,
                    searchArea
            )) {

                if (!itemEntity.getItem().is(Items.LAPIS_LAZULI)) {
                    continue;
                }

                itemEntity.getItem().shrink(1);

                if (itemEntity.getItem().isEmpty()) {
                    itemEntity.discard();
                }

                LunarWaterConversion.convertToLunarWater(level, pos);

                break;
            }
        }
    }

    @SubscribeEvent
    public static void onCauldronPlaced(BlockEvent.EntityPlaceEvent event) {

        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        BlockPos pos = event.getBlockSnapshot().getPos();
        BlockState state = event.getPlacedBlock();

        if (state.getBlock() != Blocks.CAULDRON) {
            return;
        }

        LunarWaterConversion.trackCauldron(
                level,
                pos
        );
    }
}
