package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class StarShower extends CelestialEvent {

    public StarShower() {
        super(
                "star_shower",
                new CelestialEventConfig(
                        () -> Config.STAR_SHOWERS.get(),
                        () -> Config.STAR_SHOWERS_CHANCE.get(),
                        () -> Config.STAR_SHOWERS_START_NIGHT.get(),
                        () -> Config.STAR_SHOWERS_INTERVAL.get(),
                        () -> Config.STAR_SHOWERS_GRACE_DAYS.get()
                )
        );
    }

    @Override
    public Component getStartMessage() {
        return Component.translatable(
                "info." + NyxROTL.MODID + ".star_shower"
        ).withStyle(
                ChatFormatting.GOLD,
                ChatFormatting.ITALIC
        );
    }

    @Override
    public boolean shouldStart(Level level, boolean lastDaytime, boolean forced) {
        if (!lastDaytime || level.isDay()) {
            return false;
        }

        return canStart(level, forced);
    }

    @Override
    public boolean shouldStop(Level level, boolean lastDaytime) {
        return level.isDay();
    }

    @Override
    public int getSkyColor() {
        return Config.colorStarShower.get();
    }

    @Override
    public float getSkyModifier() {
        return 0.5F;
    }

    @Override
    public float getMoonSizeMultiplier() {
        return 1.5F;
    }

    @Override
    public void tick(Level level, boolean lastDaytime) {
        // Star Shower currently has no per-tick event logic.
        // Meteor/falling-star spawning is handled elsewhere.
    }
}