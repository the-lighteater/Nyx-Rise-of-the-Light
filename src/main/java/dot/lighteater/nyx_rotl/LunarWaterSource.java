package dot.lighteater.nyx_rotl;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public enum LunarWaterSource {
    BOTTLE,
    STANDING,
    CAULDRON;

    public static final Set<LunarWaterSource> NONE = EnumSet.noneOf(LunarWaterSource.class);

    public static final Set<LunarWaterSource> ALL = EnumSet.allOf(LunarWaterSource.class);

    private static final String[] NAMES = Arrays.stream(values())
            .map(Enum::name)
            .toArray(String[]::new);

    public static String[] getNames() {
        return NAMES.clone(); // safe defensive copy
    }

    public static boolean containsName(String s) {
        if (s == null) return false;
        return Arrays.stream(values())
                .anyMatch(e -> e.name().equals(s));
    }
}