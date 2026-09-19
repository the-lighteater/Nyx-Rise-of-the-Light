package dot.lighteater.nyx_rotl.event;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.LunarWaterConversion;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.entities.FallingMeteor;
import dot.lighteater.nyx_rotl.entities.FallingStar;
import dot.lighteater.nyx_rotl.entities.WolfAISpecialMoon;
import dot.lighteater.nyx_rotl.item.ModItems;
import dot.lighteater.nyx_rotl.network.PacketHandler;
import dot.lighteater.nyx_rotl.network.PacketNyxWorld;
import dot.lighteater.nyx_rotl.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
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

import java.util.Iterator;

import static dot.lighteater.nyx_rotl.Config.*;

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

        // Sync world data to players.
        if (entity instanceof ServerPlayer player) {
            PacketNyxWorld packet = new PacketNyxWorld(
                    nyx.currentEvent,
                    nyx.eventSkyColor,
                    nyx.eventSkyModifier,
                    nyx.meteorLandingSites,
                    nyx.cachedMeteorPositions
            );

            PacketHandler.sendToPlayer(player, packet);
        }

        // Inject wolf AI.
        if (entity instanceof Wolf wolf) {

            // Avoid double-adding task.
            if (!hasMoonTask(wolf)) {
                wolf.targetSelector.addGoal(
                        3,
                        new WolfAISpecialMoon(wolf)
                );
            }
        }
    }

    private static boolean hasMoonTask(Wolf wolf) {
        return wolf.targetSelector.getAvailableGoals().stream()
                .anyMatch(goal -> goal.getGoal() instanceof WolfAISpecialMoon);
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

        NyxWorld data = NyxWorld.get(level);

        data.tick(level);

        //spawnFallingStars(level, data);

        spawnMeteors(level, data);
        processCachedMeteors(level, data);

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

    private static void spawnFallingStars(ServerLevel level, NyxWorld data) {

        // Only attempt once per second.
        if (level.getGameTime() % 20 != 0) {
            return;
        }

        // Only spawn at night.
        if (level.isDay()) {
            return;
        }

        // Falling stars must be enabled.
        if (!Config.fallingStars.get()) {
            return;
        }

        for (Player player : level.players()) {

            // Star Shower makes falling stars 15x more common.
            float chanceMultiplier =
                    "star_shower".equals(data.currentEvent) ? 15.0F : 1.0F;

            float random = level.random.nextFloat();

            if (random > Config.fallingStarRarity.get().floatValue() * chanceMultiplier) {
                continue;
            }

            // Pick a position roughly 20 blocks around the player.
            BlockPos startPos = player.blockPosition().offset(
                    Mth.floor(level.random.nextGaussian() * 20),
                    0,
                    Mth.floor(level.random.nextGaussian() * 20)
            );

            // Put the star blocks above the terrain.
            startPos = level.getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    startPos
            ).above(
                    Mth.nextInt(level.random, 40, 80)
            );

            FallingStar star = new FallingStar(
                    ModEntities.FALLING_STAR.get(),
                    level
            );

            star.setPos(
                    startPos.getX(),
                    startPos.getY(),
                    startPos.getZ()
            );

            level.addFreshEntity(star);
        }
    }

    private static void spawnMeteors(ServerLevel level, NyxWorld data) {
        if (level.getGameTime() % 20 != 0) {
            return;
        }

        if (!Config.meteors.get()) {
            return;
        }

        if (level.players().isEmpty()) {
            return;
        }

        Player player = level.players().get(
                level.random.nextInt(level.players().size())
        );

        int radius = Config.meteorSpawnRadius.get();

        int offsetX = Mth.floor(
                level.random.nextDouble() * (radius * 2 + 1) - radius
        );

        int offsetZ = Mth.floor(
                level.random.nextDouble() * (radius * 2 + 1) - radius
        );

        BlockPos spawnPos = player.blockPosition().offset(
                offsetX,
                0,
                offsetZ
        );

        double chance = Config.getMeteorChance(level, data);

        // Reduce meteor chance based on how long players have been
        // present around this chunk.
        ChunkPos meteorChunk = new ChunkPos(spawnPos);

        int presentTicks = data.playersPresentTicks.getOrDefault(
                meteorChunk,
                0
        );

        if (presentTicks > 0) {
            chance *= Math.pow(
                    0.5,
                    (double) presentTicks / Config.meteorDisallowTime.get()
            );
        }

        if (chance <= 0 || level.random.nextDouble() > chance) {
            return;
        }

        NyxROTL.LOGGER.debug(
                "[Meteor] Spawn roll succeeded | player={} | pos={} | chance={} | presentTicks={}",
                player.getName().getString(),
                spawnPos,
                chance,
                presentTicks
        );

        boolean loaded = level.hasChunkAt(spawnPos);

        NyxROTL.LOGGER.debug(
                "[Meteor] Chunk check | pos={} | chunk={} | loaded={}",
                spawnPos,
                new ChunkPos(spawnPos),
                loaded
        );

        // If the target area isn't loaded, remember it and spawn later.
        if (!loaded) {
            data.cachedMeteorPositions.add(spawnPos);
            data.setDirty();

            NyxROTL.LOGGER.debug(
                    "[Meteor] Cached unloaded meteor position: {}",
                    spawnPos
            );

            return;
        }

        FallingMeteor.spawn(
                level,
                spawnPos,
                ModEntities.FALLING_METEOR.get()
        );
    }

    private static void processCachedMeteors(
            ServerLevel level,
            NyxWorld data
    ) {
        if (data.cachedMeteorPositions.isEmpty()) {
            return;
        }

        Iterator<BlockPos> iterator =
                data.cachedMeteorPositions.iterator();

        while (iterator.hasNext()) {

            BlockPos pos = iterator.next();

            if (!level.hasChunkAt(pos)) {
                continue;
            }

            NyxROTL.LOGGER.debug(
                    "[Meteor] Loading cached meteor at {}",
                    pos
            );

            FallingMeteor.spawn(
                    level,
                    pos,
                    ModEntities.FALLING_METEOR.get()
            );

            iterator.remove();
            data.setDirty();
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
