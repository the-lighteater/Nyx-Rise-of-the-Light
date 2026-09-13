package dot.lighteater.nyx_rotl.lunarevents;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public abstract class LunarEvent {

    public final String name;

    public LunarEvent(String name) {
        this.name = name;
    }

    public abstract Component getStartMessage();

    public abstract boolean shouldStart(Level level, boolean lastDaytime);

    public abstract boolean shouldStop(Level level, boolean lastDaytime);

    public int getSkyColor() {
        return 0;
    }

    public String getMoonTexture() {
        return null;
    }

    public void tick(Level level, boolean lastDaytime) {
    }
}