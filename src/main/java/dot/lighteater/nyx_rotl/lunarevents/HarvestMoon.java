package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.lunarevents.CelestialEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HarvestMoon extends CelestialEvent {

    public HarvestMoon() {
        super(
                "harvest_moon",
                new CelestialEventConfig(
                        () -> Config.HARVEST_MOON.get(),
                        () -> Config.HARVEST_MOON_CHANCE.get(),
                        () -> Config.HARVEST_MOON_START_NIGHT.get(),
                        () -> Config.HARVEST_MOON_INTERVAL.get(),
                        () -> Config.HARVEST_MOON_GRACE_PERIOD.get()
                )
        );
    }

    @Override
    public Component getStartMessage() {
        return Component.translatable(
                "info.nyx_rotl.harvest_moon"
        ).withStyle(style -> style
                .withColor(0x5555FF)
                .withItalic(true)
        );
    }

    @Override
    public boolean shouldStart(Level level, boolean lastDaytime, boolean forced) {
        if (lastDaytime || level.isDay()) {
            return false;
        }

        if (Config.HARVEST_MOON_ON_FULL.get()
                && level.getMoonPhase() != 0) {
            return false;
        }

        return canStart(level, forced);
    }

    @Override
    public boolean shouldStop(Level level, boolean lastDaytime) {
        // Harvest Moon ends when daytime begins.
        return level.isDay();
    }

    @Override
    public int getSkyColor() {
        return Config.HARVEST_MOON_COLOR.get();
    }

    @Override
    public float getSkyModifier() {
        return 0.5f;
    }

    @Override
    public void tick(Level level, boolean lastDaytime) {
        if (level.isClientSide()) {
            return;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Only grow crops while Harvest Moon is actually active.
        // The event manager controls this through currentEvent.
        if (Config.HARVEST_MOON_GROW_AMOUNT.get() <= 0) {
            return;
        }

        if (Config.HARVEST_MOON_GROW_INTERVAL.get() <= 0) {
            return;
        }

        if (serverLevel.getGameTime() % Config.HARVEST_MOON_GROW_INTERVAL.get() != 0) {
            return;
        }

        growLoadedChunks(serverLevel);
    }

    private void growLoadedChunks(ServerLevel level) {
        Set<ChunkPos> processedChunks = new HashSet<>();

        for (var player : level.players()) {
            ChunkPos chunkPos = new ChunkPos(player.blockPosition());

            if (!processedChunks.add(chunkPos)) {
                continue;
            }

            LevelChunk chunk = level.getChunkAt(player.blockPosition());

            for (int i = 0; i < Config.HARVEST_MOON_GROW_AMOUNT.get(); i++) {
                tryGrowRandomBlock(level, chunk);
            }
        }
    }

    private void tryGrowRandomBlock(ServerLevel level, LevelChunk chunk) {
        int localX = level.random.nextInt(16);
        int localZ = level.random.nextInt(16);

        int worldX = chunk.getPos().getMinBlockX() + localX;
        int worldZ = chunk.getPos().getMinBlockZ() + localZ;

        BlockPos pos = level.getHeightmapPos(
                net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                new BlockPos(worldX, 0, worldZ)
        );

        BlockState state = level.getBlockState(pos);

        /*
         * Preserve the old behavior:
         *
         * - Must be growable
         * - Don't grow grass
         * - Don't grow tall grass
         * - Don't grow double plants
         */
        if (!(state.getBlock() instanceof BonemealableBlock growable)) {
            return;
        }

        if (state.is(Blocks.GRASS)
                || state.is(Blocks.TALL_GRASS)
                || state.is(Blocks.LARGE_FERN)
                || state.is(Blocks.FERN)) {
            return;
        }

        if (!growable.isValidBonemealTarget(level, pos, state, true)) {
            return;
        }

        if (!growable.isBonemealSuccess(level, level.random, pos, state)) {
            return;
        }

        growable.performBonemeal(
                level,
                level.random,
                pos,
                state
        );
    }
}