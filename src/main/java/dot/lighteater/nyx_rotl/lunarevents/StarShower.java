package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class StarShower extends LunarEvent {

    public StarShower() {
        super("star_shower");
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
    public boolean shouldStart(Level level, boolean lastDaytime) {
        // Temporary test:
        // Start when night begins.
        return lastDaytime && !level.isDay();
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
    public void tick(Level level, boolean lastDaytime) {
        // Nothing yet.
    }
}