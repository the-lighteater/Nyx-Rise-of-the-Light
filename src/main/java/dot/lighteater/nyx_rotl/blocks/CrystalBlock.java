package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CrystalBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 12, 12);

    public CrystalBlock(Properties properties) {
        super(properties);
    }

    // -----------------------------------
    // RANDOM TICK (crop growth boosting)
    // -----------------------------------
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity be = level.getBlockEntity(pos);

        if (!(be instanceof CrystalBlockEntity crystal)) {
            return;
        }

        if (crystal.durability <= 0) {
            return;
        }

        int range = 5;

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = -1; y <= 1; y++) {

                    BlockPos offset = pos.offset(x, y, z);
                    BlockState targetState = level.getBlockState(offset);
                    Block block = targetState.getBlock();

                    if (block == this) continue;

                    if (block.isRandomlyTicking(targetState)) {

                        for (int i = 0; i < 5; i++) {
                            block.randomTick(
                                    targetState,
                                    level,
                                    offset,
                                    random
                            );

                            // Stop if the crystal has run out
                            if (crystal.durability <= 0) {
                                return;
                            }
                        }

                        // Check whether the block changed as a result
                        if (level.getBlockState(offset) != targetState) {

                            NyxROTL.LOGGER.debug(
                                    "[Crystal] Growth detected at {}: {} -> {}",
                                    offset,
                                    level.getBlockState(offset),
                                    targetState
                            );

                            crystal.durability--;

                            if (crystal.durability <= 0) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    // -----------------------------------
    // BLOCK ENTITY
    // -----------------------------------
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof CrystalBlockEntity crystal) {
                CrystalBlockEntity.tick(lvl, pos, st, crystal);
            }
        };
    }

    // -----------------------------------
    // SHAPE
    // -----------------------------------
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}