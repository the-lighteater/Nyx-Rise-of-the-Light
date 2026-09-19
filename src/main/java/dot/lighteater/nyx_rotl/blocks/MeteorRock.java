package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class MeteorRock extends Block {

    // private final Supplier<Item> droppedItem;

    public MeteorRock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(40.0F, 3000.0F)
        );
    }

    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        // Only remove the landing site if this Meteor Rock
        // is actually being replaced by a different block.
        if (!state.is(newState.getBlock())) {
            if (level instanceof ServerLevel serverLevel) {
                NyxWorld data = NyxWorld.get(serverLevel);

                data.meteorLandingSites.remove(pos);
                data.sendToClients();
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }



    @Override
    public void stepOn(
            Level level,
            BlockPos pos,
            BlockState state,
            Entity entity
    ) {
        if (!entity.fireImmune()
                && entity instanceof LivingEntity livingEntity
                && !EnchantmentHelper.hasFrostWalker(livingEntity)) {

            entity.hurt(
                    level.damageSources().hotFloor(),
                    1.0F
            );
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void animateTick(
            BlockState state,
            Level level,
            BlockPos pos,
            RandomSource random
    ) {
        for (int i = 0; i < 3; i++) {

            boolean side = random.nextBoolean();

            double x = side
                    ? random.nextDouble()
                    : random.nextBoolean() ? 1.0 : 0.0;

            double z = !side
                    ? random.nextDouble()
                    : random.nextBoolean() ? 1.0 : 0.0;

            double y = random.nextBoolean()
                    ? 1.0
                    : 0.0;

            level.addParticle(
                    ParticleTypes.ASH,
                    pos.getX() + x,
                    pos.getY() + y,
                    pos.getZ() + z,
                    0.0,
                    0.0,
                    0.0
            );
        }

        super.animateTick(state, level, pos, random);
    }
}