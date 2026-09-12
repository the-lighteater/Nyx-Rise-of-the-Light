package dot.lighteater.nyx_rotl.lunarevents;

import dot.lighteater.nyx_rotl.Config;
import net.minecraft.world.level.Level;

public class LunarEventData {

    public int daysSinceLast;
    public int startDays;
    public int graceDays;

    public void tick(boolean justTurnedDay, Config.LunarEventConfig config, boolean isActive) {

        if (isActive) {
            daysSinceLast = 0;
            graceDays = 0;
        }

        if (justTurnedDay) {
            daysSinceLast++;

            if (startDays < config.startNight) startDays++;
            if (graceDays < config.graceDays) graceDays++;
        }
    }

    public boolean canStart(LunarEvent event, Config.LunarEventConfig config, boolean forced, Level level) {

        if (forced) return true;

        if (startDays < config.startNight) return false;
        if (graceDays < config.graceDays) return false;

        if (config.nightInterval > 0) {
            return daysSinceLast >= config.nightInterval;
        }

        return level.random.nextDouble() <= config.chance;
    }
}