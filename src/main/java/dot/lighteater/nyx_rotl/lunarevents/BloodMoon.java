package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class BloodMoon extends LunarEvent {
    public BloodMoon() {
        super("blood_moon");
    }

    // 🌟 start message
    @Override
    public Component getStartMessage() {
        return Component.translatable("info." + NyxROTL.MODID + ".blood_moon")
                .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC);
    }

    // 🌙 start condition
    @Override
    public boolean shouldStart(Level level, boolean lastDaytime) {
        // if (Config.bloodMoonOnFull && level.getMoonPhase() < 1) return false;

        if (!lastDaytime || level.isDay()) return false;

        // delegate to config system (now externalized)
        return this.shouldStart(level, lastDaytime);
    }

    // 🌞 stop condition
    @Override
    public boolean shouldStop(Level level, boolean lastDaytime) {
        return level.isDay();
    }

    // 🎨 sky tint
    @Override
    public int getSkyColor() {
        return Config.colorStarShower.get();
    }

    // tick hook (optional behavior per event)
    @Override
    public void tick(Level level, LunarEventData data, boolean lastDaytime) {
        this.tick(level, data, lastDaytime);
    }
}
