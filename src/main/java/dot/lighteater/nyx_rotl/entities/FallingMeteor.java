package dot.lighteater.nyx_rotl.entities;

import com.mojang.logging.LogUtils;
import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.lunarevents.HarvestMoon;
import dot.lighteater.nyx_rotl.registry.ModEntities;
import dot.lighteater.nyx_rotl.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import net.minecraft.util.Mth;

import java.util.List;

public class FallingMeteor extends Entity {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(
                    FallingMeteor.class,
                    EntityDataSerializers.INT
            );

    private float trajectoryX;
    private float trajectoryY;
    private float trajectoryZ;

    public boolean homing;
    public boolean disableMessage;
    public float speedModifier = 2f;
    public boolean spawnNoBlocks;

    public FallingMeteor(
            EntityType<? extends FallingMeteor> type,
            Level level
    ) {
        super(type, level);

        this.noPhysics = true;

        initTrajectory(2 * speedModifier);
    }

    // -----------------------------------
    // ENTITY DATA
    // -----------------------------------

    @Override
    protected void defineSynchedData() {
        this.entityData.define(SIZE, 1);
    }

    // -----------------------------------
    // SPAWN INITIALIZATION
    // -----------------------------------

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        if (entityData.get(SIZE) <= 0) {
            entityData.set(SIZE, 2);
        }

        if (speedModifier <= 0) {
            speedModifier = 1;
        }

        if (trajectoryIsZero()) {
            initTrajectory(2 * speedModifier);
        }
    }

    private boolean trajectoryIsZero() {
        return getDeltaMovement().lengthSqr() == 0;
    }

    // -----------------------------------
    // POSITION OVERRIDE
    // -----------------------------------

    @Override
    public void moveTo(
            double x,
            double y,
            double z,
            float yRot,
            float xRot
    ) {
        if (homing) {
            y += 48;
        }

        super.moveTo(x, y, z, yRot, xRot);
    }

    // -----------------------------------
    // TICK
    // -----------------------------------

    @Override
    public void tick() {
        super.tick();

        if (!isLoaded()) {
            discard();
            return;
        }

        if (level().isClientSide) {
            spawnParticles();
            return;
        }

        tickMovement();

        if (level().getGameTime() % 40 == 0) {
            level().playSound(
                    null,
                    blockPosition(),
                    ModSounds.fallingMeteorSound.get(),
                    SoundSource.WEATHER,
                    5f,
                    1f
            );
        }
    }

    protected void tickMovement() {
        ServerLevel level = (ServerLevel) this.level();

        if (getY() <= -64) {
            discard();
            return;
        }

        Vec3 start = position();
        Vec3 end = start.add(getDeltaMovement());

        BlockHitResult hit = level.clip(
                new ClipContext(
                        start,
                        end,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this
                )
        );

        if (hit.getType() != HitResult.Type.MISS) {
            setPos(
                    hit.getLocation().x,
                    hit.getLocation().y,
                    hit.getLocation().z
            );

            onImpact(level);
            return;
        }

        setPos(end.x, end.y, end.z);

        handleHoming(level);
    }

    // -----------------------------------
    // LOAD CHECK
    // -----------------------------------

    public boolean isLoaded() {
        BlockPos pos = blockPosition();

        return level().hasChunksAt(
                pos.offset(-34, -34, -34),
                pos.offset(34, 34, 34)
        );
    }

    // -----------------------------------
    // TRAJECTORY
    // -----------------------------------

    private void initTrajectory(float multiplier) {

        trajectoryX =
                Mth.nextFloat(random, 0.5F, 1.25F) * multiplier;

        trajectoryY =
                Mth.nextFloat(random, -0.85F, -0.5F) * multiplier;

        trajectoryZ =
                Mth.nextFloat(random, 0.5F, 1.25F) * multiplier;

        if (random.nextBoolean()) {
            trajectoryX *= -1;
        }

        if (random.nextBoolean()) {
            trajectoryZ *= -1;
        }

        setDeltaMovement(
                trajectoryX,
                trajectoryY,
                trajectoryZ
        );
    }

    // -----------------------------------
    // HOMING
    // -----------------------------------

    private void handleHoming(ServerLevel level) {

        if (!homing) {
            return;
        }

        Player player = level.getNearestPlayer(this, 128);

        if (player == null) {
            return;
        }

        if (player.distanceToSqr(this) < 32 * 32) {
            return;
        }

        Vec3 dir = player.position()
                .subtract(position())
                .normalize();

        setDeltaMovement(
                dir.x * 2 * speedModifier,
                Math.min(
                        dir.y * 2 * speedModifier,
                        getDeltaMovement().y
                ),
                dir.z * 2 * speedModifier
        );
    }

    // -----------------------------------
    // IMPACT
    // -----------------------------------

    private void onImpact(ServerLevel level) {

        if (removeTrees(blockPosition())) {
            return;
        }

        LOGGER.debug("Hit solid ground...");

        Explosion explosion = level.explode(
                this,
                getX(),
                getY(),
                getZ(),
                entityData.get(SIZE) * 4,
                true,
                Level.ExplosionInteraction.BLOCK
        );

        if (!spawnNoBlocks) {
            processExplosionBlocks(
                    level,
                    explosion.getToBlow()
            );
        }

        // Spawn MeteorKats at the meteor impact site.
        for (int katCount = 0;
             katCount < Config.meteorKatCount.get();
             katCount++) {

            if (level.random.nextDouble() < Config.meteorKatChance.get()) {

                MeteorKat kat = ModEntities.METEOR_KAT.get().create(level);

                if (kat != null) {
                    kat.setPos(
                            getX(),
                            getY() + 1,
                            getZ()
                    );

                    level.addFreshEntity(kat);
                }
            }
        }

        LOGGER.debug("Processing explosion...");

        discard();

        if (!disableMessage) {
            broadcastImpact(level);
        }

        LOGGER.debug("Sending message...");
    }

    // -----------------------------------
    // BLOCK PROCESSING
    // -----------------------------------

    private void processExplosionBlocks(
            ServerLevel level,
            List<BlockPos> affected
    ) {

        NyxWorld data = NyxWorld.get(level);

        for (BlockPos pos : affected) {

            BlockState state = level.getBlockState(pos);
            BlockState below = level.getBlockState(pos.below());

            if (!state.canBeReplaced() || !below.isSolid()) {
                continue;
            }

            if (level.random.nextBoolean()) {
                continue;
            }

            if (level.random.nextInt(5) == 0) {
                level.setBlock(
                        pos,
                        Blocks.MAGMA_BLOCK.defaultBlockState(),
                        3
                );
            } else {
                if (data.getCurrentEvent() instanceof HarvestMoon && level.random.nextInt(10) == 0) {
                    level.setBlock(
                            pos,
                            ModBlocks.GLEANING_METEOR_ROCK.get().defaultBlockState(),
                            3
                    );
                } else {
                    level.setBlock(
                            pos,
                            ModBlocks.METEOR_ROCK.get().defaultBlockState(),
                            3
                    );
                }
                data.meteorLandingSites.add(pos);
                data.sendToClients();
            }
        }
    }

    // -----------------------------------
    // MESSAGING
    // -----------------------------------

    private void broadcastImpact(ServerLevel level) {

        List<ServerPlayer> players = level.players();

        for (ServerPlayer player : players) {

            double dist = player.distanceToSqr(this);

            if (dist <= 256 * 256) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable(
                                "info.nyx_rotl.meteor"
                        ),
                        true
                );
            }
        }

        // Play the impact sound at the actual impact location.
        level.playSound(
                null,
                blockPosition(),
                ModSounds.fallingMeteorImpactSound.get(),
                SoundSource.WEATHER,
                10f,
                1f
        );
    }

    // -----------------------------------
    // PARTICLES
    // -----------------------------------

    private void spawnParticles() {

        for (int i = 0; i < 30; i++) {

            level().addParticle(
                    ParticleTypes.FLAME,
                    getX(),
                    getY(),
                    getZ(),
                    (random.nextDouble() - 0.5) * 0.5,
                    (random.nextDouble() - 0.5) * 0.5,
                    (random.nextDouble() - 0.5) * 0.5
            );
        }
    }

    // -----------------------------------
    // SPAWN
    // -----------------------------------

    public static FallingMeteor spawn(
            ServerLevel level,
            BlockPos pos,
            EntityType<FallingMeteor> type
    ) {

        BlockPos spawnPos = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING,
                pos
        ).above(
                level.random.nextInt(64, 96)
        );

        FallingMeteor meteor =
                new FallingMeteor(type, level);

        meteor.setPos(
                spawnPos.getX(),
                spawnPos.getY(),
                spawnPos.getZ()
        );

        level.addFreshEntity(meteor);

        return meteor;
    }

    // -----------------------------------
    // SAVE / LOAD
    // -----------------------------------

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

        tag.putFloat("trajectory_x", trajectoryX);
        tag.putFloat("trajectory_y", trajectoryY);
        tag.putFloat("trajectory_z", trajectoryZ);

        tag.putInt(
                "size",
                entityData.get(SIZE)
        );

        tag.putBoolean(
                "homing",
                homing
        );

        tag.putBoolean(
                "disable_message",
                disableMessage
        );

        tag.putFloat(
                "speed",
                speedModifier
        );

        tag.putBoolean(
                "spawn_no_blocks",
                spawnNoBlocks
        );
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

        if (tag.contains("trajectory_x")) {

            trajectoryX = tag.getFloat("trajectory_x");
            trajectoryY = tag.getFloat("trajectory_y");
            trajectoryZ = tag.getFloat("trajectory_z");

            setDeltaMovement(
                    trajectoryX,
                    trajectoryY,
                    trajectoryZ
            );

        } else {
            initTrajectory(2 * speedModifier);
        }

        entityData.set(
                SIZE,
                tag.getInt("size")
        );

        homing = tag.getBoolean("homing");

        disableMessage =
                tag.getBoolean("disable_message");

        speedModifier =
                tag.getFloat("speed");

        spawnNoBlocks =
                tag.getBoolean("spawn_no_blocks");

        if (speedModifier <= 0) {
            speedModifier = 1;
        }
    }

    // -----------------------------------
    // TREE REMOVAL
    // -----------------------------------

    private boolean removeTrees(BlockPos pos) {

        boolean any = false;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {

                    BlockPos offset = pos.offset(x, y, z);

                    if (offset.distSqr(pos) >= 64) {
                        continue;
                    }

                    BlockState state = level().getBlockState(offset);

                    if (!state.is(BlockTags.LOGS)
                            && !state.is(BlockTags.LEAVES)) {
                        continue;
                    }

                    level().destroyBlock(
                            offset,
                            false
                    );

                    any = true;
                }
            }
        }

        return any;
    }

    // -----------------------------------
    // SIZE
    // -----------------------------------

    public void setSize(int size) {
        entityData.set(SIZE, size);
    }

    public int getSize() {
        return entityData.get(SIZE);
    }
}