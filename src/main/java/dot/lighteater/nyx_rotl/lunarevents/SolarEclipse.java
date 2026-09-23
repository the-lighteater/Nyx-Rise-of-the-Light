package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class SolarEclipse extends CelestialEvent {

    private static final int ECLIPSE_DURATION = 12000;

    private int elapsedTicks = 0;
    private boolean active = false;

    public SolarEclipse() {
        super(
                "solar_eclipse",
                new CelestialEventConfig(
                        () -> Config.SOLAR_ECLIPSE.get(),
                        () -> Config.SOLAR_ECLIPSE_CHANCE.get(),
                        () -> Config.SOLAR_ECLIPSE_START_DAY.get(),
                        () -> Config.SOLAR_ECLIPSE_INTERVAL.get(),
                        () -> Config.SOLAR_ECLIPSE_GRACE_DAYS.get()
                )
        );
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
    public boolean shouldStart(Level level, boolean lastDaytime, boolean forced) {
        // Solar eclipses begin when night transitions into day.
        if (lastDaytime || !level.isDay()) {
            return false;
        }

        return canStart(level, forced);
    }

    @Override
    public boolean shouldStop(Level level, boolean lastDaytime) {
        return !active || elapsedTicks >= ECLIPSE_DURATION;
    }

    @Override
    public void onStart(Level level) {
        elapsedTicks = 0;
        active = true;
    }

    @Override
    public void onStop(Level level) {
        elapsedTicks = 0;
        active = false;
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
        if (!active) {
            return;
        }

        elapsedTicks++;
    }

    @Override
    public float getMoonEclipseOffset() {
        float progress = getProgress();

        return (progress - 0.5F) * 20.0F;
    }

    @Override
    public float getMoonEclipseAngle() {
        float progress = getProgress();

        return 180.0F + (progress - 0.5F) * 180.0F;
    }

    public void reset() {
        elapsedTicks = 0;
    }
}