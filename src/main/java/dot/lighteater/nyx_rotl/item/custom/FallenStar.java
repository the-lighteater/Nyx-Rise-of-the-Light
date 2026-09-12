package dot.lighteater.nyx_rotl.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class FallenStar extends Item {

    private static final String FALLEN_STAR_TAG = "nyx:fallen_star";
    private static final String LAST_ON_GROUND_TAG = "nyx:last_on_ground";

    public FallenStar(Properties props) {
        super(props);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();
        CompoundTag tag = entity.getPersistentData();

        // 🌟 Client-side: particles only
        if (level.isClientSide) {
            if (tag.getBoolean(FALLEN_STAR_TAG) && level.random.nextFloat() >= 0.7F) {
                double mX = level.random.nextGaussian() * 0.05;
                double mY = level.random.nextFloat() * 0.4;
                double mZ = level.random.nextGaussian() * 0.05;

                level.addParticle(
                        ParticleTypes.FIREWORK,
                        entity.getX(),
                        entity.getY() + 0.5,
                        entity.getZ(),
                        mX, mY, mZ
                );
            }
            return false;
        }

        // ❌ Not a "fallen" star → ignore
        if (!tag.getBoolean(FALLEN_STAR_TAG)) return false;

        // 🌞 Daytime → remove
        if (level.isDay()) {
            entity.discard();
            return true;
        }

        // 🌍 Landing detection (only trigger once)
        boolean lastOnGround = tag.getBoolean(LAST_ON_GROUND_TAG);

        if (entity.onGround() && !lastOnGround) {
            tag.putBoolean(LAST_ON_GROUND_TAG, true);
            placeStarBlock(level, entity.blockPosition());
        }

        return false;
    }

    private void placeStarBlock(Level level, BlockPos pos) {

        // TODO: Replace with your custom starAir block
        if (level.getBlockState(pos).canBeReplaced()) {
            level.setBlockAndUpdate(pos, Blocks.DIAMOND_BLOCK.defaultBlockState());
            return;
        }

        // Search nearby (3x3x3 cube)
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {

                    BlockPos offset = pos.offset(x, y, z);

                    if (level.getBlockState(offset).canBeReplaced()) {
                        level.setBlockAndUpdate(offset, Blocks.DIAMOND_BLOCK.defaultBlockState());
                        return;
                    }
                }
            }
        }
    }
}