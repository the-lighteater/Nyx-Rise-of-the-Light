package dot.lighteater.nyx_rotl.entities;

import com.mojang.logging.LogUtils;
import dot.lighteater.nyx_rotl.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.List;

public class FallingMeteor extends FallingStar {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(FallingMeteor.class, EntityDataSerializers.INT);

    public boolean homing;
    public boolean disableMessage;
    public float speedModifier = 1f;
    public boolean spawnNoBlocks;

    public FallingMeteor(EntityType<? extends FallingMeteor> type, Level level) {
        super(type, level);
        initTrajectory(2 * speedModifier);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 1);
    }

    // -------------------------
    // SPAWN INITIALIZATION
    // -------------------------
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
        return this.getDeltaMovement().lengthSqr() == 0;
    }

    // -------------------------
    // POSITION OVERRIDE
    // -------------------------
    @Override
    public void moveTo(double x, double y, double z, float yRot, float xRot) {
        if (homing) y += 48;
        super.moveTo(x, y, z, yRot, xRot);
    }

    // -------------------------
    // MAIN TICK LOGIC
    // -------------------------
    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            spawnParticles();
            return;
        }

        ServerLevel level = (ServerLevel) this.level();

        // void kill
        if (getY() <= -64) {
            discard();
            return;
        }

        handleHoming(level);

        if (horizontalCollision || verticalCollision) {
            onImpact(level);
        } else {
            if (level.getGameTime() % 35 == 0) {
                level.playSound(
                        null,
                        blockPosition(),
                        ModSounds.fallingMeteorSound.get(),
                        SoundSource.AMBIENT,
                        5f,
                        1f
                );
            }
        }
    }

    // -------------------------
    // HOMING
    // -------------------------
    private void handleHoming(ServerLevel level) {
        if (!homing) return;

        Player player = level.getNearestPlayer(this, 128);
        if (player == null) return;

        if (player.distanceToSqr(this) < 32 * 32) return;

        Vec3 dir = player.position().subtract(position()).normalize();

        setDeltaMovement(
                dir.x * 2 * speedModifier,
                Math.min(dir.y * 2 * speedModifier, getDeltaMovement().y),
                dir.z * 2 * speedModifier
        );
    }

    // -------------------------
    // IMPACT LOGIC
    // -------------------------
    private void onImpact(ServerLevel level) {

        if (removeTrees(blockPosition())) {
            return;
        }

        LOGGER.debug("Hit solid ground...");

        Explosion explosion = level.explode(
                this,
                getX(), getY(), getZ(),
                entityData.get(SIZE) * 4,
                false,
                Level.ExplosionInteraction.BLOCK
        );

        if (!spawnNoBlocks) {
            processExplosionBlocks(level, explosion.getToBlow());
        }

        LOGGER.debug("Processing explosion...");

        this.discard();

        if (!disableMessage) {
            broadcastImpact(level);
        }

        LOGGER.debug("Sending message...");
    }

    // -------------------------
    // BLOCK PROCESSING
    // -------------------------
    private void processExplosionBlocks(ServerLevel level, List<BlockPos> affected) {
        for (BlockPos pos : affected) {

            BlockState state = level.getBlockState(pos);
            BlockState below = level.getBlockState(pos.below());

            if (!state.canBeReplaced() || !below.isSolid()) continue;
            if (level.random.nextBoolean()) continue;

            if (level.random.nextInt(5) == 0) {
                level.setBlock(pos, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
            } else {
                level.setBlock(pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
            }
        }
    }

    // -------------------------
    // MESSAGING
    // -------------------------
    private void broadcastImpact(ServerLevel level) {

        List<ServerPlayer> players = level.players();

        for (Player player : players) {
            double dist = player.distanceToSqr(this);

            if (dist <= 256 * 256) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.translatable("info.nyx_rotl.meteor"),
                        true
                );
            }

            level.playSound(
                    null,
                    player.blockPosition(),
                    ModSounds.fallingMeteorImpactSound.get(),
                    SoundSource.AMBIENT,
                    0.5f,
                    0.15f
            );
        }
    }

    // -------------------------
    // PARTICLES
    // -------------------------
    private void spawnParticles() {
        for (int i = 0; i < 30; i++) {
            level().addParticle(
                    net.minecraft.core.particles.ParticleTypes.FLAME,
                    getX(), getY(), getZ(),
                    (random.nextDouble() - 0.5) * 0.5,
                    (random.nextDouble() - 0.5) * 0.5,
                    (random.nextDouble() - 0.5) * 0.5
            );
        }
    }

    public static FallingMeteor spawn(ServerLevel level, BlockPos pos, EntityType<FallingMeteor> type) {
        BlockPos spawnPos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, pos)
                .above(level.random.nextInt(64, 96));

        FallingMeteor meteor = new FallingMeteor(type, level);
        meteor.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

        level.addFreshEntity(meteor);
        return meteor;
    }

    // -------------------------
    // SAVE / LOAD
    // -------------------------
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("size", entityData.get(SIZE));
        tag.putBoolean("homing", homing);
        tag.putBoolean("disable_message", disableMessage);
        tag.putFloat("speed", speedModifier);
        tag.putBoolean("spawn_no_blocks", spawnNoBlocks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(SIZE, tag.getInt("size"));
        homing = tag.getBoolean("homing");
        disableMessage = tag.getBoolean("disable_message");
        speedModifier = tag.getFloat("speed");
        spawnNoBlocks = tag.getBoolean("spawn_no_blocks");
    }

    // -------------------------
    // TREE REMOVAL (UNCHANGED LOGIC)
    // -------------------------
    private boolean removeTrees(BlockPos pos) {
        boolean any = false;

        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++) {

                    BlockPos offset = pos.offset(x, y, z);

                    if (offset.distSqr(this.blockPosition()) >= 64) continue;

                    BlockState state = level().getBlockState(offset);

                    if (!state.isCollisionShapeFullBlock(level(), offset)) continue;

                    level().destroyBlock(offset, false);
                    any = true;
                }

        return any;
    }

    public void setSize(int size) {
        this.entityData.set(SIZE, size);
    }

    public int getSize() {
        return this.entityData.get(SIZE);
    }
}