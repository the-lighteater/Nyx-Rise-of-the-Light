package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class SolarEclipse extends CelestialEvent {

    private static final int ECLIPSE_DURATION = 12000;

    private int elapsedTicks = 0;

    public SolarEclipse() {
        super("solar_eclipse");
    }

    public void reset() {
        elapsedTicks = 0;
    }

    @Override
    public Component getStartMessage() {
        return Component.translatable(
                "info." + NyxROTL.MODID + ".solar_eclipse"
        ).withStyle(
                ChatFormatting.DARK_GRAY,
                ChatFormatting.BOLD
        );
    }

    @Override
    public boolean isSolarEvent() {
        return true;
    }

    @Override
    public boolean shouldStart(Level level, boolean lastDaytime) {
        return !lastDaytime && level.isDay();
    }

    @Override
    public boolean shouldStop(Level level, boolean lastDaytime) {
        return elapsedTicks >= ECLIPSE_DURATION;
    }

    @Override
    public int getSkyColor() {
        return Config.colorRedSupergiant.get();
    }

    @Override
    public float getSkyModifier() {
        return 0.5F;
    }

    @Override
    public String getSunTexture() {
        return "red_supergiant";
    }

    @Override
    public String getMoonTexture() {
        return "eclipse_moon";
    }

    @Override
    public float getSunSizeMultiplier() {
        return 1.0F;
    }

    @Override
    public float getMoonSizeMultiplier() {
        return 1.5F;
    }

    @Override
    public float getProgress() {
        return Math.min(
                1.0F,
                (float) elapsedTicks / ECLIPSE_DURATION
        );
    }

    @Override
    public float getIntensity() {
        float progress = getProgress();

        if (progress < 0.5F) {
            return progress * 2.0F;
        }

        return (1.0F - progress) * 2.0F;
    }

    public boolean isTotality() {
        return getIntensity() >= 0.95F;
    }

    @Override
    public void tick(Level level, boolean lastDaytime) {
        elapsedTicks++;
    }

    @Override
    public float getMoonEclipseOffset() {
        float progress = getProgress();

        // Start to the left.
        // Finish to the right.
        return (progress - 0.5F) * 20.0F;
    }

    @Override
    public float getMoonEclipseAngle() {
        float progress = getProgress();

        // Moon travels from one side of the sky,
        // directly across the sun, to the other side.
        //
        // Start:    90°
        // Totality: 180°
        // End:      270°
        return 180.0F + (progress - 0.5F) * 180.0F;
    }
}