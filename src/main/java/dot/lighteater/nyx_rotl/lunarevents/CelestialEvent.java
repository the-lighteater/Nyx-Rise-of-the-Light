package dot.lighteater.nyx_rotl.lunarevents;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public abstract class CelestialEvent {

    public final String name;

    public CelestialEvent(String name) {
        this.name = name;
    }

    public abstract Component getStartMessage();

    public abstract boolean shouldStart(
            Level level,
            boolean lastDaytime
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

    /**
     * Color used for event sky/fog tinting.
     */
    public int getSkyColor() {
        return 0;
    }

    /**
     * Strength of the sky/fog tint.
     *
     * 0.0 = no tint
     * 1.0 = completely replace the original color
     */
    public float getSkyModifier() {
        return 0f;
    }

    /**
     * Custom moon texture.
     *
     * Return null to use the vanilla moon.
     */
    public String getMoonTexture() {
        return null;
    }

    /**
     * Custom sun texture.
     *
     * Return null to use the vanilla sun.
     */
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
        return 1.0f;
    }

    public float getProgress() {
        return 1.0f;
    }

    public void tick(Level level, boolean lastDaytime) {
    }

    /**
     * Additional horizontal movement of the moon during this event.
     *
     * 0.0 = normal moon position.
     * Positive/negative values move the moon across the sky.
     */
    public float getMoonEclipseOffset() {
        return 0.0F;
    }

    /**
     * Additional rotation of the moon during this event.
     *
     * Measured in degrees.
     */
    public float getMoonEclipseAngle() {
        return 0.0F;
    }
}