package dot.lighteater.nyx_rotl.blocks;

import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class MeteorGlassBlock extends GlassBlock {
    public MeteorGlassBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_GRAY)
                .strength(5.0f, 3000.0f)
                .sound(SoundType.GLASS)
                .noOcclusion()
        );
    }
}
