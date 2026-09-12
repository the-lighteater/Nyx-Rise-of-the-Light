package dot.lighteater.nyx_rotl.blocks;

import net.minecraft.core.BlockPos;
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

    /*
     * TODO:
     * When meteor landing sites are moved to SavedData,
     * remove this block's position from the saved landing-site
     * collection when the block is broken.
     *
     * Old 1.12.2 behavior:
     *
     * NyxWorld data = NyxWorld.get(world);
     * if (data != null) {
     *     data.meteorLandingSites.remove(pos);
     *     data.sendToClients();
     * }
     */

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