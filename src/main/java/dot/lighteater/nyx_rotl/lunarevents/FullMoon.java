package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class FullMoon extends CelestialEvent {

    public FullMoon() {
        super("full_moon");
    }

    @Override
    public Component getStartMessage() {
        return Component.translatable(
                "info." + NyxROTL.MODID + ".full_moon"
        ).withStyle(
                ChatFormatting.GRAY,
                ChatFormatting.ITALIC
        );
    }

    @Override
    public boolean shouldStart(Level level, boolean lastDaytime, boolean forced) {
        if (!Config.fullMoon.get()) {
            return false;
        }

        // Must be the start of a new night.
        if (!lastDaytime || level.isDay()) {
            return false;
        }

        // Moon phase 0 is the vanilla full moon.
        return level.getMoonPhase() == 0;
    }

    @Override
    public boolean shouldStop(Level level, boolean lastDaytime) {
        return level.isDay();
    }
}