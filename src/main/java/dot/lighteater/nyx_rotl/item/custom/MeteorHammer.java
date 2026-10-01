package dot.lighteater.nyx_rotl.item.custom;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class MeteorHammer extends PickaxeItem {

    private static final int MAX_USE_TIME = 72000;
    private static final int MIN_USE_TIME = 20;

    public MeteorHammer(
            Tier tier,
            int attackDamageModifier,
            float attackSpeedModifier,
            Properties properties
    ) {
        super(
                tier,
                attackDamageModifier,
                attackSpeedModifier,
                properties
        );
    }

    // ------------------------------------------------------------
    // Right click / charging
    // ------------------------------------------------------------

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        player.startUsingItem(hand);

        return InteractionResultHolder.sidedSuccess(
                player.getItemInHand(hand),
                level.isClientSide
        );
    }

    @Override
    public void releaseUsing(
            ItemStack stack,
            Level level,
            LivingEntity entity,
            int timeLeft
    ) {
        if (!(entity instanceof Player player)) {
            return;
        }

        // Original behavior:
        // The player must be standing on the ground.
        if (!player.onGround()) {
            return;
        }

        int useTime =
                getUseDuration(stack) - timeLeft;

        if (useTime < MIN_USE_TIME) {
            return;
        }

        // Original behavior:
        // Player has to be looking upward.
        if (player.getXRot() > -10.0F) {
            return;
        }

        float modifier = Mth.clamp(
                (useTime - MIN_USE_TIME) / 5.0F,
                1.0F,
                2.5F
        );

        // --------------------------------------------------------
        // Launch player
        // --------------------------------------------------------

        float yawRadians =
                player.getYRot() * ((float) Math.PI / 180.0F);

        player.setDeltaMovement(
                player.getDeltaMovement().x
                        - modifier * Mth.sin(yawRadians),

                player.getDeltaMovement().y
                        + 0.625D * modifier,

                player.getDeltaMovement().z
                        + modifier * Mth.cos(yawRadians)
        );

        player.getPersistentData().putLong(
                NyxROTL.MODID + ":leap_start",
                level.getGameTime()
        );

        // --------------------------------------------------------
        // Server effects
        // --------------------------------------------------------

        if (!level.isClientSide) {

            level.playSound(
                    null,
                    player.blockPosition(),
                    ModSounds.hammerStartSound.get(),
                    SoundSource.PLAYERS,
                    0.5F,
                    1.0F
            );

            if (level instanceof ServerLevel serverLevel) {

                serverLevel.sendParticles(
                        net.minecraft.core.particles.ParticleTypes.FLAME,
                        player.getX(),
                        player.getY() + player.getEyeHeight(),
                        player.getZ(),
                        30,
                        0.25D,
                        0.25D,
                        0.25D,
                        0.05D
                );
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return MAX_USE_TIME;
    }

    // ------------------------------------------------------------
    // Multi-block breaking
    // ------------------------------------------------------------

    @Override
    public boolean onBlockStartBreak(
            ItemStack stack,
            BlockPos pos,
            Player player
    ) {
        // Sneaking uses normal pickaxe behavior.
        if (player.isShiftKeyDown()) {
            return false;
        }

        if (player.level().isClientSide) {
            return false;
        }

        double reach = player.getAttributeValue(
                ForgeRegistries.ATTRIBUTES.getValue(
                        new ResourceLocation("forge", "block_reach")
                ));

        HitResult hitResult =
                player.pick(
                        reach,
                        1.0F,
                        false
                );

        if (!(hitResult instanceof BlockHitResult blockHit)) {
            return false;
        }

        Direction side = blockHit.getDirection();

        return breakBlocks(
                stack,
                1,
                player.level(),
                pos,
                side,
                player
        );
    }

    private boolean breakBlocks(
            ItemStack stack,
            int radius,
            Level level,
            BlockPos originalPos,
            Direction side,
            Player player
    ) {
        int xRange = radius;
        int yRange = radius;
        int zRange = 0;

        // Same orientation logic as the original.
        if (side.getAxis() == Direction.Axis.Y) {
            zRange = radius;
            yRange = 0;
        }

        if (side.getAxis() == Direction.Axis.X) {
            xRange = 0;
            zRange = radius;
        }

        BlockState originalState =
                level.getBlockState(originalPos);

        float mainHardness =
                originalState.getDestroySpeed(
                        level,
                        originalPos
                );

        // Break the block that was actually targeted first.
        if (!tryHarvestBlock(
                level,
                originalPos,
                false,
                stack,
                player
        )) {
            return false;
        }

        if (radius == 2
                && side.getAxis() != Direction.Axis.Y) {

            BlockPos above =
                    originalPos.above();

            BlockState state =
                    level.getBlockState(above);

            float hardness =
                    state.getDestroySpeed(
                            level,
                            above
                    );

            if (hardness <= mainHardness + 5.0F) {

                tryHarvestBlock(
                        level,
                        above,
                        true,
                        stack,
                        player
                );
            }
        }

        if (radius > 0 && mainHardness >= 0.2F) {

            for (int x = originalPos.getX() - xRange;
                 x <= originalPos.getX() + xRange;
                 x++) {

                for (int y = originalPos.getY() - yRange;
                     y <= originalPos.getY() + yRange;
                     y++) {

                    for (int z = originalPos.getZ() - zRange;
                         z <= originalPos.getZ() + zRange;
                         z++) {

                        if (x == originalPos.getX()
                                && y == originalPos.getY()
                                && z == originalPos.getZ()) {
                            continue;
                        }

                        BlockPos target =
                                new BlockPos(x, y, z);

                        BlockState state =
                                level.getBlockState(target);

                        float hardness =
                                state.getDestroySpeed(
                                        level,
                                        target
                                );

                        if (hardness <= mainHardness + 5.0F) {

                            tryHarvestBlock(
                                    level,
                                    target,
                                    true,
                                    stack,
                                    player
                            );
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean tryHarvestBlock(
            Level level,
            BlockPos pos,
            boolean extraBlock,
            ItemStack stack,
            Player player
    ) {
        BlockState state =
                level.getBlockState(pos);

        Block block =
                state.getBlock();

        float hardness =
                state.getDestroySpeed(level, pos);

        if (hardness < 0.0F) {
            return false;
        }

        boolean canHarvest =
                player.hasCorrectToolForDrops(state);

        if (extraBlock) {

            boolean fastEnough =
                    getDestroySpeed(stack, state) > 1.0F;

            if (!canHarvest || !fastEnough) {
                return false;
            }

            // Don't destroy tile entities with the extra-block pass.
            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            if (blockEntity != null) {
                return false;
            }
        }

        // Creative does not consume durability.
        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(
                    1,
                    player,
                    p -> p.broadcastBreakEvent(
                            player.getUsedItemHand()
                    )
            );
        }

        return breakExtraBlock(
                stack,
                level,
                player,
                pos
        );
    }

    private static boolean breakExtraBlock(
            ItemStack stack,
            Level level,
            Player player,
            BlockPos pos
    ) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }

        if (player.isCreative()) {
            return serverLevel.removeBlock(pos, false);
        }

        BlockState state = serverLevel.getBlockState(pos);

        if (state.isAir()) {
            return false;
        }

        BlockEntity blockEntity = serverLevel.getBlockEntity(pos);

        // Let Forge/other mods know the block is being broken.
        if (player instanceof ServerPlayer serverPlayer) {
            int exp = ForgeHooks.onBlockBreakEvent(
                    serverLevel,
                    serverPlayer.gameMode.getGameModeForPlayer(),
                    serverPlayer,
                    pos
            );

            if (exp == -1) {
                return false;
            }

            boolean removed = state.onDestroyedByPlayer(
                    serverLevel,
                    pos,
                    player,
                    true,
                    serverLevel.getFluidState(pos)
            );

            if (!removed) {
                return false;
            }

            if (blockEntity == null) {
                state.getBlock().playerDestroy(
                        serverLevel,
                        player,
                        pos,
                        state,
                        null,
                        stack
                );
            }

            if (exp > 0) {
                state.getBlock().popExperience(
                        serverLevel,
                        pos,
                        exp
                );
            }

            return true;
        }

        return false;
    }
}