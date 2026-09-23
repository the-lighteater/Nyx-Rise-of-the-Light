package dot.lighteater.nyx_rotl.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FallenStar extends Item {

    public FallenStar(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();

        /*
         * Fallen stars only exist at night.
         *
         * If the item has direct visibility of the sky, it disappears.
         * This means a fallen star must be underneath some kind of cover.
         */
        if (!level.isClientSide) {

            BlockPos pos = entity.blockPosition();
            if (level.isDay() && level.canSeeSky(pos)) {
                entity.discard();
                return true;
            }

            // Make the fallen star glow.
            entity.setGlowingTag(true);
        }

        /*
         * Client-side visual effect.
         */
        if (level.isClientSide && level.random.nextFloat() >= 0.7F) {
            level.addParticle(
                    ParticleTypes.FIREWORK,
                    entity.getX(),
                    entity.getY() + 0.5D,
                    entity.getZ(),
                    level.random.nextGaussian() * 0.05D,
                    level.random.nextFloat() * 0.4D,
                    level.random.nextGaussian() * 0.05D
            );
        }

        return false;
    }
}