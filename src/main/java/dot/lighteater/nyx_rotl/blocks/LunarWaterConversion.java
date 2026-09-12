package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.LunarWaterSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LunarWaterConversion {

    /*
     * How long a full water cauldron must remain exposed.
     *
     * Values are in Minecraft ticks.
     *
     * 24000 = one Minecraft day.
     *
     * Currently set very low for testing.
     */
    private static final int BASE_EXPOSURE_TIME = 600;

    /**
     * Checks all tracked cauldrons in this world and advances
     * their Lunar Water exposure when appropriate.
     */
    public static void tick(ServerLevel level) {

        LunarWaterSavedData data =
                LunarWaterSavedData.get(level);

        /*
         * Make a copy because cauldrons can be removed from
         * the SavedData while we are processing them.
         */
        List<BlockPos> cauldrons =
                new ArrayList<>(data.getCauldrons());

        for (BlockPos pos : cauldrons) {

            BlockState state =
                    level.getBlockState(pos);

            /*
             * The block was removed or replaced with something
             * other than a cauldron.
             */
            if (state.getBlock() != Blocks.CAULDRON
                    && state.getBlock() != Blocks.WATER_CAULDRON) {

                data.removeCauldron(pos);
                continue;
            }

            /*
             * Empty vanilla cauldron.
             *
             * Keep tracking it. Once it becomes a full water
             * cauldron, exposure can begin.
             */
            if (state.getBlock() == Blocks.CAULDRON) {
                continue;
            }

            /*
             * We have a water cauldron.
             *
             * Only a full cauldron can be incensed.
             */
            int waterLevel =
                    state.getValue(
                            LayeredCauldronBlock.LEVEL
                    );

            if (waterLevel != 3) {
                continue;
            }

            /*
             * Already fully incensed.
             */
            if (isIncensed(level, pos)) {
                spawnIncensedParticles(
                        level,
                        pos
                );
                continue;
            }

            /*
             * Must be able to see the sky.
             */
            if (!level.canSeeSky(pos.above())) {
                continue;
            }

            /*
             * Must be nighttime.
             */
            if (level.isDay()) {
                continue;
            }

            int requiredTime =
                    getRequiredExposureTime(level);

            int currentTime =
                    data.getExposure(pos);

            int newTime =
                    currentTime + 1;

            /*
             * Don't allow the stored value to go above
             * the required amount.
             */
            if (newTime >= requiredTime) {

                data.setExposure(
                        pos,
                        requiredTime
                );

                spawnIncensedParticles(
                        level,
                        pos
                );

                NyxROTL.LOGGER.debug(
                        "[LunarWaterConversion] Cauldron fully incensed | pos={} | exposure={}",
                        pos,
                        requiredTime
                );

                continue;
            }

            data.setExposure(
                    pos,
                    newTime
            );

            /*
             * Show particles approximately once per second
             * while the cauldron is being incensed.
             */
            if (newTime % 20 == 0) {

                spawnIncensedParticles(
                        level,
                        pos
                );
            }
        }
    }

    /**
     * Begins tracking a cauldron.
     *
     * The cauldron does not need to contain water yet.
     */
    public static void trackCauldron(
            ServerLevel level,
            BlockPos pos
    ) {

        LunarWaterSavedData data =
                LunarWaterSavedData.get(level);

        if (!data.isTracked(pos)) {

            data.trackCauldron(pos);

            NyxROTL.LOGGER.debug(
                    "[LunarWaterConversion] Started tracking cauldron | pos={}",
                    pos
            );
        }
    }

    /**
     * Checks whether a position contains a full vanilla
     * water cauldron and begins tracking it.
     */
    public static void checkCauldron(
            ServerLevel level,
            BlockPos pos
    ) {

        BlockState state =
                level.getBlockState(pos);

        if (state.getBlock() != Blocks.WATER_CAULDRON) {
            return;
        }

        if (state.getValue(
                LayeredCauldronBlock.LEVEL
        ) != 3) {
            return;
        }

        trackCauldron(
                level,
                pos
        );
    }

    /**
     * Returns all cauldrons that have completed their
     * Lunar Water exposure.
     */
    public static Set<BlockPos> getIncensedCauldrons(
            ServerLevel level
    ) {

        LunarWaterSavedData data =
                LunarWaterSavedData.get(level);

        int requiredTime =
                getRequiredExposureTime(level);

        java.util.Set<BlockPos> incensed =
                new java.util.HashSet<>();

        for (BlockPos pos : data.getCauldrons()) {

            if (data.getExposure(pos) >= requiredTime) {

                incensed.add(pos);
            }
        }

        return incensed;
    }

    /**
     * Returns whether a cauldron has completed its
     * required Lunar Water exposure.
     */
    public static boolean isIncensed(
            ServerLevel level,
            BlockPos pos
    ) {

        LunarWaterSavedData data =
                LunarWaterSavedData.get(level);

        if (!data.isTracked(pos)) {
            return false;
        }

        return data.getExposure(pos)
                >= getRequiredExposureTime(level);
    }

    /**
     * Converts a fully incensed vanilla water cauldron
     * into a full Lunar Water cauldron.
     */
    public static void convertToLunarWater(
            ServerLevel level,
            BlockPos pos
    ) {

        level.setBlockAndUpdate(
                pos,
                ModBlocks.LUNAR_WATER_CAULDRON
                        .get()
                        .defaultBlockState()
                        .setValue(
                                LayeredCauldronBlock.LEVEL,
                                3
                        )
        );

        LunarWaterSavedData data =
                LunarWaterSavedData.get(level);

        data.removeCauldron(pos);

        spawnConversionParticles(
                level,
                pos
        );

        NyxROTL.LOGGER.info(
                "[LunarWaterConversion] Water Cauldron converted to Lunar Water at {}",
                pos
        );
    }

    /**
     * Determines how long the cauldron needs to remain
     * exposed based on the current moon phase.
     *
     * Moon phases:
     *
     * 0 = full moon
     * 1 = waning gibbous
     * 2 = third quarter
     * 3 = waning crescent
     * 4 = new moon
     * 5 = waxing crescent
     * 6 = first quarter
     * 7 = waxing gibbous
     *
     * Full moon is fastest.
     */
    private static int getRequiredExposureTime(
            ServerLevel level
    ) {

        int phase =
                level.getMoonPhase();

        return switch (phase) {

            case 0 ->
                    BASE_EXPOSURE_TIME / 2;

            case 1, 7 ->
                    BASE_EXPOSURE_TIME;

            case 2, 6 ->
                    BASE_EXPOSURE_TIME * 2;

            case 3, 5 ->
                    BASE_EXPOSURE_TIME * 3;

            case 4 ->
                    BASE_EXPOSURE_TIME * 4;

            default ->
                    BASE_EXPOSURE_TIME;
        };
    }

    /**
     * Particles shown while the cauldron is becoming
     * incensed.
     */
    private static void spawnIncensedParticles(
            ServerLevel level,
            BlockPos pos
    ) {

        level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.CLOUD,
                pos.getX() + 0.5,
                pos.getY() + 1.05,
                pos.getZ() + 0.5,
                2,
                0.2,
                0.05,
                0.2,
                0.01
        );
    }

    /**
     * Particles shown when the water becomes Lunar Water.
     */
    private static void spawnConversionParticles(
            ServerLevel level,
            BlockPos pos
    ) {

        level.sendParticles(
                net.minecraft.core.particles.ParticleTypes.END_ROD,
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                20,
                0.3,
                0.2,
                0.3,
                0.05
        );
    }
}