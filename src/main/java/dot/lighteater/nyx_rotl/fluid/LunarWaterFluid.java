package dot.lighteater.nyx_rotl.fluid;

import dot.lighteater.nyx_rotl.blocks.ModBlocks;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class LunarWaterFluid extends ForgeFlowingFluid {
    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(() -> ModFluidTypes.LUNAR_WATER_TYPE.get(),
            () -> ModFluids.LUNAR_WATER.get(),
            () -> ModFluids.FLOWING_LUNAR_WATER.get()).explosionResistance(100f).bucket(
                    () -> ModItems.LUNAR_WATER_BUCKET.get()).block(() -> (LiquidBlock) ModBlocks.LUNAR_WATER.get());

    private LunarWaterFluid() {
        super(PROPERTIES);
    }

    public static class Source extends LunarWaterFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

    public static class Flowing extends LunarWaterFluid {
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }
}