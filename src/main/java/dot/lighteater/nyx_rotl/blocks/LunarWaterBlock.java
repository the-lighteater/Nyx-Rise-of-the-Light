package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.fluid.ModFluids;
import dot.lighteater.nyx_rotl.procedures.applyLunarWater;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class LunarWaterBlock extends LiquidBlock {
    public LunarWaterBlock() {
        super(() -> ModFluids.LUNAR_WATER.get(), BlockBehaviour.Properties.of().mapColor(MapColor.WATER).strength(100f).hasPostProcess((bs, br, bp) -> true).noCollission()
                .liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
    }

    @Override
    public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
        super.entityInside(blockstate, world, pos, entity);
        applyLunarWater.execute(entity);
    }
}