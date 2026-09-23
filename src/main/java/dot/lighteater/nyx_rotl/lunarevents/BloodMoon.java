package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.*;

public class BloodMoon extends CelestialEvent {

    private static final int MOB_COUNT_DIV = 17 * 17;

    private final Set<ChunkPos> eligibleChunksForSpawning = new HashSet<>();

    public BloodMoon() {
        super(
                "blood_moon",
                new CelestialEventConfig(
                        () -> Config.BLOOD_MOON.get(),
                        () -> Config.BLOOD_MOON_CHANCE.get(),
                        () -> Config.BLOOD_MOON_START_NIGHT.get(),
                        () -> Config.BLOOD_MOON_INTERVAL.get(),
                        () -> Config.BLOOD_MOON_GRACE_PERIOD.get()
                )
        );
    }

    @Override
    public Component getStartMessage() {
        return Component.translatable(
                "info." + NyxROTL.MODID + ".blood_moon"
        ).withStyle(
                ChatFormatting.DARK_RED,
                ChatFormatting.ITALIC
        );
    }

    @Override
    public boolean shouldStart(Level level, boolean lastDaytime, boolean forced) {

        if (Config.BLOOD_MOON_ON_FULL.get()
                && level.getMoonPhase() != 0) {
            return false;
        }

        if (!lastDaytime || level.isDay()) {
            return false;
        }

        return canStart(level, forced);
    }

    @Override
    public boolean shouldStop(Level level, boolean lastDaytime) {
        return level.isDay();
    }

    @Override
    public int getSkyColor() {
        return Config.COLOR_BLOOD_MOON.get();
    }

    @Override
    public float getSkyModifier() {
        return 1f;
    }

    @Override
    public float getMoonSizeMultiplier() {
        return 2f;
    }

    @Override
    public String getMoonTexture() {
        return "blood_moon";
    }

    @Override
    public void tick(Level level, boolean lastDaytime) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (!name.equals(getCurrentEventName(serverLevel))) {
            return;
        }

        if (Config.BLOOD_MOON_SPAWN_MULTIPLIER.get() <= 1) {
            return;
        }

        if (serverLevel.getGameTime() % 400L != 0L) {
            return;
        }

        findChunksForSpawning(serverLevel);
    }

    private String getCurrentEventName(ServerLevel level) {
        return NyxWorld.get(level).currentEvent;
    }

    private int findChunksForSpawning(ServerLevel level) {
        buildEligibleChunks(level);

        if (eligibleChunksForSpawning.isEmpty()) {
            return 0;
        }

        int spawnedTotal = 0;

        BlockPos spawnPoint = level.getSharedSpawnPos();

        /*
         * The old method iterated EnumCreatureType.values().
         *
         * Since Blood Moon calls this with:
         *
         *     spawnHostileMobs = true
         *     spawnPeacefulMobs = false
         *
         * we only need MONSTER here.
         */
        MobCategory category = MobCategory.MONSTER;

        int existing = countMobs(level, category);

        int spawnCap =
                category.getMaxInstancesPerChunk()
                        * Config.BLOOD_MOON_SPAWN_MULTIPLIER.get()
                        * eligibleChunksForSpawning.size()
                        / MOB_COUNT_DIV;

        if (existing > spawnCap) {
            return 0;
        }

        List<ChunkPos> shuffled =
                new ArrayList<>(eligibleChunksForSpawning);

        Collections.shuffle(shuffled, new java.util.Random());

        for (ChunkPos chunkPos : shuffled) {

            LevelChunk chunk = level.getChunk(
                    chunkPos.x,
                    chunkPos.z
            );

            BlockPos blockPos =
                    getRandomChunkPosition(level, chunkPos);

            BlockState state =
                    level.getBlockState(blockPos);

            if (state.isRedstoneConductor(
                    level,
                    blockPos
            )) {
                continue;
            }

            int packCount = 0;

            for (int attempt = 0; attempt < 3; attempt++) {

                int x = blockPos.getX();
                int y = blockPos.getY();
                int z = blockPos.getZ();

                SpawnGroupData spawnData = null;

                int attempts =
                        1 + level.random.nextInt(4);

                for (int i = 0; i < attempts; i++) {

                    x += level.random.nextInt(6)
                            - level.random.nextInt(6);

                    z += level.random.nextInt(6)
                            - level.random.nextInt(6);

                    // Old code effectively had no vertical
                    // movement because nextInt(1) is always 0.
                    BlockPos.MutableBlockPos mutable =
                            new BlockPos.MutableBlockPos(x, y, z);

                    double spawnX = x + 0.5D;
                    double spawnZ = z + 0.5D;

                    /*
                     * Preserve the old player-distance rule.
                     */
                    if (level.getNearestPlayer(
                            spawnX,
                            y,
                            spawnZ,
                            Config.BLOOD_MOON_SPAWN_RADIUS.get(),
                            false
                    ) != null) {
                        continue;
                    }

                    Vec3i spawnPos = new Vec3i( (int) spawnX, y, (int) spawnZ);

                    /*
                     * Preserve the old 24-block spawn-point
                     * restriction:
                     *
                     * distanceSq >= 576
                     */
                    double spawnDistance =
                            spawnPoint.distSqr(
                                    spawnPos
                            );

                    if (spawnDistance < 576.0D) {
                        continue;
                    }

                    MobSpawnSettings.SpawnerData spawnDataEntry =
                            getSpawnData(
                                    level,
                                    category,
                                    mutable
                            );

                    if (spawnDataEntry == null) {
                        continue;
                    }

                    EntityType<?> entityType =
                            spawnDataEntry.type;

                    /*
                     * Check the entity's registered spawn placement.
                     */
                    if (!SpawnPlacements.checkSpawnRules(
                            entityType,
                            level,
                            MobSpawnType.NATURAL,
                            mutable,
                            level.random
                    )) {
                        continue;
                    }

                    Mob mob;

                    try {
                        mob = (Mob) entityType.create(level);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return spawnedTotal;
                    }

                    if (mob == null) {
                        continue;
                    }

                    /*
                     * Apply our Blood Moon whitelist/blacklist.
                     */
                    String entityId =
                            BuiltInRegistries.ENTITY_TYPE
                                    .getKey(entityType)
                                    .toString();

                    boolean listed =
                            Config.BLOOD_MOON_BLACKLIST
                                    .get()
                                    .contains(entityId);

                    boolean allowed =
                            Config.IS_MOB_DUPLICATION_WHITELIST.get()
                                    == listed;

                    if (!allowed) {
                        mob.discard();
                        continue;
                    }

                    /*
                     * Mark this mob as a Blood Moon spawn.
                     *
                     * Replace this with your preferred NBT/data
                     * mechanism if other code already expects a
                     * specific Blood Moon tag.
                     */
                    mob.getPersistentData().putBoolean(
                            NyxROTL.MODID + ":blood_moon_spawn",
                            true
                    );


                    mob.moveTo(
                            spawnX,
                            y,
                            spawnZ,
                            level.random.nextFloat() * 360.0F,
                            0.0F
                    );

                    NyxROTL.LOGGER.debug("Mob Spawned: {}", mob);

                    /*
                     * Forge's modern spawn-position check.
                     */
                    if (!ForgeEventFactory.checkSpawnPosition(
                            mob,
                            level,
                            MobSpawnType.NATURAL
                    )) {
                        mob.discard();
                        continue;
                    }

                    /*
                     * Run Forge's finalize-spawn hooks.
                     */
                    SpawnGroupData finalized =
                            ForgeEventFactory.onFinalizeSpawn(
                                    mob,
                                    level,
                                    level.getCurrentDifficultyAt(
                                            mutable
                                    ),
                                    MobSpawnType.NATURAL,
                                    spawnData,
                                    null
                            );

                    if (finalized == null) {
                        mob.discard();
                        continue;
                    }

                    spawnData = finalized;

                    if (!level.noCollision(mob)) {
                        mob.discard();
                        continue;
                    }

                    if (!level.addFreshEntity(mob)) {
                        mob.discard();
                        continue;
                    }

                    packCount++;
                    spawnedTotal++;

                    if (packCount >=
                            ForgeEventFactory.getMaxSpawnPackSize(mob)) {
                        break;
                    }
                }
            }
        }

        return spawnedTotal;
    }

    private static MobSpawnSettings.SpawnerData getSpawnData(
            ServerLevel level,
            MobCategory category,
            BlockPos pos
    ) {
        return level.getChunkSource()
                .getGenerator()
                .getMobsAt(
                        level.getBiome(pos),
                        level.structureManager(),
                        category,
                        pos
                )
                .getRandom(level.random)
                .orElse(null);
    }

    private static int countMobs(
            ServerLevel level,
            MobCategory category
    ) {
        int count = 0;

        for (net.minecraft.world.entity.Entity entity :
                level.getAllEntities()) {

            if (entity instanceof Mob mob
                    && mob.getType().getCategory() == category) {
                count++;
            }
        }

        return count;
    }

    private static BlockPos getRandomChunkPosition(
            ServerLevel level,
            ChunkPos chunkPos
    ) {
        int x = chunkPos.getMinBlockX()
                + level.random.nextInt(16);

        int z = chunkPos.getMinBlockZ()
                + level.random.nextInt(16);

        int maxY = level.getHeight(
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                x,
                z
        );

        if (maxY <= level.getMinBuildHeight()) {
            return new BlockPos(
                    x,
                    level.getMinBuildHeight(),
                    z
            );
        }

        int y = level.random.nextInt(
                Math.max(1, maxY - level.getMinBuildHeight())
        ) + level.getMinBuildHeight();

        return new BlockPos(x, y, z);
    }

    private void buildEligibleChunks(ServerLevel level) {
        eligibleChunksForSpawning.clear();

        for (ServerPlayer player : level.players()) {
            if (player.isSpectator()) {
                continue;
            }

            ChunkPos center = player.chunkPosition();

            for (int x = -8; x <= 8; x++) {
                for (int z = -8; z <= 8; z++) {
                    boolean border =
                            x == -8 || x == 8 ||
                                    z == -8 || z == 8;

                    if (border) {
                        continue;
                    }

                    ChunkPos chunkPos = new ChunkPos(
                            center.x + x,
                            center.z + z
                    );

                    if (!level.getWorldBorder().isWithinBounds(chunkPos)) {
                        continue;
                    }

                    // Only use chunks that are already loaded.
                    if (level.getChunkSource().hasChunk(
                            chunkPos.x,
                            chunkPos.z
                    )) {
                        eligibleChunksForSpawning.add(chunkPos);
                    }
                }
            }
        }
    }
}