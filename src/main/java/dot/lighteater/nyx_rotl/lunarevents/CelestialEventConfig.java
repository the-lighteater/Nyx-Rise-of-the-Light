package dot.lighteater.nyx_rotl.lunarevents;

import net.minecraft.nbt.CompoundTag;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

public class CelestialEventConfig {

    private final BooleanSupplier enabled;
    private final DoubleSupplier chance;
    private final IntSupplier startNight;
    private final IntSupplier nightInterval;
    private final IntSupplier gracePeriod;

    private int daysSinceLast;
    private int startDays;
    private int graceDays;

    public CelestialEventConfig(
            BooleanSupplier enabled,
            DoubleSupplier chance,
            IntSupplier startNight,
            IntSupplier nightInterval,
            IntSupplier gracePeriod
    ) {
        this.enabled = enabled;
        this.chance = chance;
        this.startNight = startNight;
        this.nightInterval = nightInterval;
        this.gracePeriod = gracePeriod;
    }

    public void update(
            boolean lastDaytime,
            boolean isDaytime,
            boolean active
    ) {
        /*
         * While the event is active, its cooldown is reset.
         */
        if (active) {
            daysSinceLast = 0;
            graceDays = 0;
        }

        /*
         * Detect the transition from night -> day.
         *
         * This is the same behavior as the old ConfigImpl.
         */
        if (!lastDaytime && isDaytime) {
            daysSinceLast++;

            if (startDays < startNight.getAsInt()) {
                startDays++;
            }

            if (graceDays < gracePeriod.getAsInt()) {
                graceDays++;
            }
        }
    }

    public boolean canStart(
            boolean forced,
            double randomValue
    ) {
        if (!enabled.getAsBoolean()) {
            return false;
        }

        if (forced) {
            return true;
        }

        if (startDays < startNight.getAsInt()) {
            return false;
        }

        if (graceDays < gracePeriod.getAsInt()) {
            return false;
        }

        /*
         * An interval overrides random chance.
         */
        if (nightInterval.getAsInt() > 0) {
            return daysSinceLast >= nightInterval.getAsInt();
        }

        return randomValue <= chance.getAsDouble();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("days_since_last", daysSinceLast);
        tag.putInt("start_days", startDays);
        tag.putInt("grace_days", graceDays);

        return tag;
    }

    public void load(CompoundTag tag) {
        daysSinceLast = tag.getInt("days_since_last");
        startDays = tag.getInt("start_days");
        graceDays = tag.getInt("grace_days");
    }

    public int getDaysSinceLast() {
        return daysSinceLast;
    }

    public int getStartDays() {
        return startDays;
    }

    public int getGraceDays() {
        return graceDays;
    }
}