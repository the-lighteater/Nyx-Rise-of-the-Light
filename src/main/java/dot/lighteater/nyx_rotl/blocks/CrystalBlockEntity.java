package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.lunarevents.HarvestMoon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

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

            if (data.getCurrentEvent() instanceof HarvestMoon &&
                    level.canSeeSky(pos.above())) {

                be.setDurability(Math.min(
                                Config.crystalDurability.get(),
                                be.durability + Config.crystalDurability.get() / 10
                        ));
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("durability", durability);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        durability = tag.getInt("durability");
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(
            Connection connection,
            ClientboundBlockEntityDataPacket packet
    ) {
        CompoundTag tag = packet.getTag();

        if (tag != null) {
            handleUpdateTag(tag);
        }
    }

    public void setDurability(int newDurability) {
        if (this.durability == newDurability) {
            return;
        }

        this.durability = newDurability;
        setChanged();

        if (level instanceof ServerLevel serverLevel) {
            BlockState state = getBlockState();

            serverLevel.sendBlockUpdated(
                    worldPosition,
                    state,
                    state,
                    Block.UPDATE_CLIENTS
            );
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