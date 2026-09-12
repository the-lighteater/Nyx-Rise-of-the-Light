package dot.lighteater.nyx_rotl.lunarevents;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;


public abstract class LunarEvent {

    public final String name;

    public LunarEvent(String name) {
        this.name = name;
    }

    // 🌙 lifecycle
    public abstract Component getStartMessage();

    public abstract boolean shouldStart(Level level, boolean lastDaytime);

    public abstract boolean shouldStop(Level level, boolean lastDaytime);

    // optional visuals
    public int getSkyColor() {
        return 0;
    }

    public String getMoonTexture() {
        return null;
    }

    // tick hook
    public void tick(Level level, LunarEventData data, boolean lastDaytime) {
    }
}