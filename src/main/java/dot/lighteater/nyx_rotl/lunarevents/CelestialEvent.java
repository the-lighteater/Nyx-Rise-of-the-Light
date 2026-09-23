package dot.lighteater.nyx_rotl.lunarevents;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public abstract class CelestialEvent {

    public final String name;

    protected final CelestialEventConfig config;

    /**
     * Creates an event that uses a scheduler configuration.
     */
    public CelestialEvent(
            String name,
            CelestialEventConfig config
    ) {
        this.name = name;
        this.config = config;
    }

    /**
     * Creates an event without scheduler configuration.
     *
     * Use this for events that have their own start/stop conditions,
     * such as Full Moon.
     */
    public CelestialEvent(String name) {
        this.name = name;
        this.config = null;
    }

    public abstract Component getStartMessage();

    public abstract boolean shouldStart(
            Level level,
            boolean lastDaytime,
            boolean forced
    );

    public abstract boolean shouldStop(
            Level level,
            boolean lastDaytime
    );

    public void onStart(Level level) {
    }

    public void onStop(Level level) {
    }

    public boolean isSolarEvent() {
        return false;
    }

    public int getSkyColor() {
        return 0;
    }

    public float getSkyModifier() {
        return 0.25f;
    }

    public String getMoonTexture() {
        return null;
    }

    public String getSunTexture() {
        return null;
    }

    public float getMoonSizeMultiplier() {
        return 1.0F;
    }

    public float getSunSizeMultiplier() {
        return 1.0F;
    }

    public float getIntensity() {
        return 1.0F;
    }

    public float getProgress() {
        return 1.0F;
    }

    public void tick(Level level, boolean lastDaytime) {
    }

    public float getMoonEclipseOffset() {
        return 0.0F;
    }

    public float getMoonEclipseAngle() {
        return 0.0F;
    }

    /**
     * Updates the scheduler configuration if this event has one.
     */
    public void updateConfig(
            boolean lastDaytime,
            boolean isDaytime,
            boolean active
    ) {
        if (config == null) {
            return;
        }

        config.update(
                lastDaytime,
                isDaytime,
                active
        );
    }

    /**
     * Checks whether this event's scheduler allows it to start.
     *
     * Events without a config cannot be started through the scheduler.
     * They should implement their own shouldStart() logic instead.
     */
    public boolean canStart(
            Level level,
            boolean forced
    ) {
        if (config == null) {
            return false;
        }

        return config.canStart(
                forced,
                level.random.nextDouble()
        );
    }

    public CelestialEventConfig getConfig() {
        return config;
    }

    /**
     * Saves scheduler state if this event has a config.
     */
    public void saveConfig(CompoundTag parent) {
        if (config == null) {
            return;
        }

        parent.put(
                name,
                config.save()
        );
    }

    /**
     * Loads scheduler state if this event has a config.
     */
    public void loadConfig(CompoundTag parent) {
        if (config == null || !parent.contains(name)) {
            return;
        }

        config.load(
                parent.getCompound(name)
        );
    }
}