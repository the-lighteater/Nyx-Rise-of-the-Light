package dot.lighteater.nyx_rotl.entities;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.item.ModItems;
import dot.lighteater.nyx_rotl.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FallingStar extends Entity {

    private float trajectoryX;
    private float trajectoryY;
    private float trajectoryZ;

    public FallingStar(
            EntityType<?> type,
            Level level
    ) {
        super(type, level);

        this.noPhysics = false;

        initTrajectory(1);
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

        // -----------------------------------
        // CLIENT
        // -----------------------------------

        if (level().isClientSide) {
            spawnParticles();
            return;
        }

        // -----------------------------------
        // MOVEMENT / IMPACT
        // -----------------------------------

        tickMovement();

        // -----------------------------------
        // AMBIENT SOUND
        // -----------------------------------

        if (tickCount % 40 == 0) {
            level().playSound(
                    null,
                    blockPosition(),
                    ModSounds.fallingStarSound.get(),
                    SoundSource.WEATHER,
                    Config.fallingStarAmbientVolume.get().floatValue(),
                    1.0f
            );
        }
    }

    // -----------------------------------
    // MOVEMENT
    // -----------------------------------

    protected void tickMovement() {

        Vec3 start = position();
        Vec3 end = start.add(getDeltaMovement());

        BlockHitResult hit = level().clip(
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

            handleImpact();
            return;
        }

        // No collision — move directly to the next position.
        setPos(
                end.x,
                end.y,
                end.z
        );
    }

    // -----------------------------------
    // IMPACT
    // -----------------------------------

    protected void handleImpact() {

        level().playSound(
                null,
                blockPosition(),
                ModSounds.fallingStarImpactSound.get(),
                SoundSource.WEATHER,
                Config.fallingStarImpactVolume.get().floatValue(),
                1.0f
        );

        ItemEntity item = new ItemEntity(
                level(),
                getX(),
                getY(),
                getZ(),
                new ItemStack(ModItems.FALLEN_STAR.get())
        );

        item.getPersistentData().putBoolean(
                NyxROTL.MODID + ":fallen_star",
                true
        );

        level().addFreshEntity(item);

        discard();
    }

    // -----------------------------------
    // PARTICLES
    // -----------------------------------

    protected void spawnParticles() {

        Vec3 motion = getDeltaMovement();

        for (int i = 0; i < 2; i++) {

            double mX =
                    -motion.x
                            + random.nextGaussian() * 0.05;

            double mY =
                    -motion.y
                            + random.nextGaussian() * 0.05;

            double mZ =
                    -motion.z
                            + random.nextGaussian() * 0.05;

            level().addParticle(
                    ParticleTypes.FIREWORK,
                    getX(),
                    getY(),
                    getZ(),
                    mX,
                    mY,
                    mZ
            );
        }
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

    public void initTrajectory(float multiplier) {

        trajectoryX =
                Mth.nextFloat(
                        random,
                        0.5F,
                        1.25F
                ) * multiplier;

        trajectoryY =
                Mth.nextFloat(
                        random,
                        -0.85F,
                        -0.5F
                ) * multiplier;

        trajectoryZ =
                Mth.nextFloat(
                        random,
                        0.5F,
                        1.25F
                ) * multiplier;

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
    // SAVE / LOAD
    // -----------------------------------

    @Override
    protected void addAdditionalSaveData(
            CompoundTag tag
    ) {

        tag.putFloat(
                "trajectory_x",
                trajectoryX
        );

        tag.putFloat(
                "trajectory_y",
                trajectoryY
        );

        tag.putFloat(
                "trajectory_z",
                trajectoryZ
        );
    }

    @Override
    protected void readAdditionalSaveData(
            CompoundTag tag
    ) {

        if (tag.contains("trajectory_x")) {

            trajectoryX =
                    tag.getFloat("trajectory_x");

            trajectoryY =
                    tag.getFloat("trajectory_y");

            trajectoryZ =
                    tag.getFloat("trajectory_z");

            setDeltaMovement(
                    trajectoryX,
                    trajectoryY,
                    trajectoryZ
            );

        } else {

            initTrajectory(1);
        }
    }

    // -----------------------------------
    // REQUIRED OVERRIDES
    // -----------------------------------

    @Override
    protected void defineSynchedData() {
    }
}