package dot.lighteater.nyx_rotl.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LunarWaterSavedData extends SavedData {

    private static final String DATA_NAME = "nyx_rotl_lunar_water";

    private static final String CAULDRONS_TAG = "Cauldrons";

    private static final String X_TAG = "X";
    private static final String Y_TAG = "Y";
    private static final String Z_TAG = "Z";
    private static final String EXPOSURE_TAG = "Exposure";

    private final Map<BlockPos, Integer> exposureTime = new HashMap<>();

    public LunarWaterSavedData() {
    }

    /**
     * Gets the LunarWaterSavedData for this world.
     *
     * Each ServerLevel has its own SavedData storage, so
     * cauldrons in different dimensions are kept separate.
     */
    public static LunarWaterSavedData get(
            net.minecraft.server.level.ServerLevel level
    ) {
        return level.getDataStorage().computeIfAbsent(
                LunarWaterSavedData::load,
                LunarWaterSavedData::new,
                DATA_NAME
        );
    }

    /**
     * Loads saved cauldron data from disk.
     */
    public static LunarWaterSavedData load(CompoundTag tag) {

        LunarWaterSavedData data = new LunarWaterSavedData();

        ListTag cauldronList =
                tag.getList(CAULDRONS_TAG, Tag.TAG_COMPOUND);

        for (int i = 0; i < cauldronList.size(); i++) {

            CompoundTag cauldronTag =
                    cauldronList.getCompound(i);

            BlockPos pos = new BlockPos(
                    cauldronTag.getInt(X_TAG),
                    cauldronTag.getInt(Y_TAG),
                    cauldronTag.getInt(Z_TAG)
            );

            int exposure =
                    cauldronTag.getInt(EXPOSURE_TAG);

            data.exposureTime.put(pos, exposure);
        }

        return data;
    }

    /**
     * Saves all tracked cauldrons to disk.
     */
    @Override
    public CompoundTag save(CompoundTag tag) {

        ListTag cauldronList = new ListTag();

        for (Map.Entry<BlockPos, Integer> entry :
                exposureTime.entrySet()) {

            BlockPos pos = entry.getKey();

            CompoundTag cauldronTag =
                    new CompoundTag();

            cauldronTag.putInt(
                    X_TAG,
                    pos.getX()
            );

            cauldronTag.putInt(
                    Y_TAG,
                    pos.getY()
            );

            cauldronTag.putInt(
                    Z_TAG,
                    pos.getZ()
            );

            cauldronTag.putInt(
                    EXPOSURE_TAG,
                    entry.getValue()
            );

            cauldronList.add(cauldronTag);
        }

        tag.put(
                CAULDRONS_TAG,
                cauldronList
        );

        return tag;
    }

    /**
     * Adds a cauldron to the tracking map if it isn't already tracked.
     */
    public void trackCauldron(BlockPos pos) {

        if (!exposureTime.containsKey(pos)) {

            exposureTime.put(
                    pos.immutable(),
                    0
            );

            setDirty();
        }
    }

    /**
     * Removes a cauldron from tracking.
     */
    public void removeCauldron(BlockPos pos) {

        if (exposureTime.remove(pos) != null) {
            setDirty();
        }
    }

    /**
     * Checks whether a cauldron is currently being tracked.
     */
    public boolean isTracked(BlockPos pos) {
        return exposureTime.containsKey(pos);
    }

    /**
     * Gets the current exposure time for a cauldron.
     *
     * Returns 0 if the cauldron isn't being tracked.
     */
    public int getExposure(BlockPos pos) {

        return exposureTime.getOrDefault(
                pos,
                0
        );
    }

    /**
     * Sets the exposure time for a cauldron.
     */
    public void setExposure(
            BlockPos pos,
            int exposure
    ) {

        Integer previous =
                exposureTime.put(
                        pos.immutable(),
                        exposure
                );

        if (previous == null
                || previous != exposure) {

            setDirty();
        }
    }

    /**
     * Increases a cauldron's exposure time by one tick.
     */
    public void incrementExposure(BlockPos pos) {

        int current =
                exposureTime.getOrDefault(
                        pos,
                        0
                );

        exposureTime.put(
                pos.immutable(),
                current + 1
        );

        setDirty();
    }

    /**
     * Gets all tracked cauldron positions.
     *
     * Returns a copy so callers cannot accidentally modify
     * the SavedData's internal map.
     */
    public Set<BlockPos> getCauldrons() {

        if (exposureTime.isEmpty()) {
            return Collections.emptySet();
        }

        return Set.copyOf(exposureTime.keySet());
    }

    /**
     * Gets all tracked cauldrons and their exposure times.
     */
    public Map<BlockPos, Integer> getExposureTimes() {

        if (exposureTime.isEmpty()) {
            return Collections.emptyMap();
        }

        return Map.copyOf(exposureTime);
    }

    /**
     * Clears all tracked cauldrons.
     */
    public void clear() {

        if (!exposureTime.isEmpty()) {

            exposureTime.clear();

            setDirty();
        }
    }
}