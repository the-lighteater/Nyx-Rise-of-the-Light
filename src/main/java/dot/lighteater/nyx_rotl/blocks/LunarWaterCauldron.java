package dot.lighteater.nyx_rotl.blocks;


import dot.lighteater.nyx_rotl.procedures.applyLunarWater;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class LunarWaterCauldron extends LayeredCauldronBlock {

    public LunarWaterCauldron() {
        super(
                BlockBehaviour.Properties.copy(Blocks.CAULDRON)
                        .lightLevel(state -> 12),
                precipitation -> false,
                LunarWaterCauldronInteractions.INTERACTIONS
        );
    }

    @Override
    public void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity
    ) {
        if (level.isClientSide()) {
            return;
        }

        if (!isEntityInsideContent(state, pos, entity)) {
            return;
        }

        boolean didSomething = false;

        // Match the old cauldron: extinguish burning entities.
        if (entity.isOnFire()) {
            entity.clearFire();
            didSomething = true;
        }

        // Apply Lunar Water effects.
        if (entity instanceof net.minecraft.world.entity.LivingEntity livingEntity) {
            if (applyLunarWater.execute(livingEntity)) {
                didSomething = true;
            }
        }

        // Only consume a level if Lunar Water actually did something.
        if (didSomething && entity.mayInteract(level, pos)) {
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
        }
    }
}