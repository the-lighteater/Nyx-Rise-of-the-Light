package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalBlockEntity extends BlockEntity {

    public int durability = 500;

    public CrystalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRYSTAL.get(), pos, state);
    }

    // -----------------------------------
    // TICK LOGIC
    // -----------------------------------
    public static void tick(Level level, BlockPos pos, BlockState state, CrystalBlockEntity be) {

        if (!level.isClientSide && level.getGameTime() % 600 == 0) {

            NyxWorld data = NyxWorld.get((ServerLevel) level);

            if (
                    //data.currentEvent instanceof HarvestMoon &&
                    level.canSeeSky(pos.above())) {

                be.durability = Math.min(
                        Config.crystalDurability.get(),
                        be.durability + Config.crystalDurability.get() / 10
                );
            }
        }
    }

    // -----------------------------------
    // SAVE / LOAD
    // -----------------------------------
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("durability", durability);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        durability = tag.getInt("durability");
    }
}