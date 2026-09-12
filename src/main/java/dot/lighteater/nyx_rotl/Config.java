package dot.lighteater.nyx_rotl;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = NyxROTL.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> allowedDimensions;
    public static final ForgeConfigSpec.BooleanValue enchantments;
    public static final ForgeConfigSpec.BooleanValue lunarWater;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> lunarWaterItemParts;
    public static List<String> lunarWaterItem;

    public static final ForgeConfigSpec.BooleanValue addPotionEffects;
    public static final ForgeConfigSpec.IntValue additonalMobsChance;

    public static final ForgeConfigSpec.DoubleValue maxLunarEdgeXpMult;
    public static final ForgeConfigSpec.DoubleValue minLevelLunarEdgeDamage;
    public static final ForgeConfigSpec.DoubleValue maxLevelLunarEdgeDamage;
    public static final ForgeConfigSpec.DoubleValue baseLunarEdgeDamage;
    public static final ForgeConfigSpec.BooleanValue disallowDayEnchanting;
    public static final ForgeConfigSpec.DoubleValue meteorShardGuardianChance;
    public static final ForgeConfigSpec.BooleanValue fallingStars;
    public static final ForgeConfigSpec.DoubleValue fallingStarRarity;
    public static final ForgeConfigSpec.DoubleValue fallingStarRarityShower;
    public static final ForgeConfigSpec.DoubleValue fallingStarImpactVolume;
    public static final ForgeConfigSpec.DoubleValue fallingStarAmbientVolume;
    public static final ForgeConfigSpec.BooleanValue fullMoon;
    public static final ForgeConfigSpec.BooleanValue bloodMoonSleeping;
//    public static final ForgeConfigSpec.IntValue bloodMoonSpawnMultiplier;
//    public static final ForgeConfigSpec.ConfigValue<Set<String>> mobDuplicationBlacklist;
//    public static final ForgeConfigSpec.BooleanValue isMobDuplicationWhitelist;
//    public static final ForgeConfigSpec.BooleanValue bloodMoonVanish;
//    public static final ForgeConfigSpec.IntValue bloodMoonSpawnRadius;
//    public static final ForgeConfigSpec.BooleanValue harvestMoonOnFull;
//    public static final ForgeConfigSpec.BooleanValue bloodMoonOnFull;
//    public static final ForgeConfigSpec.BooleanValue moonEventTint;
//    public static final ForgeConfigSpec.IntValue harvestMoonGrowAmount;
//    public static final ForgeConfigSpec.IntValue harvestMoonGrowInterval;
//    public static final ForgeConfigSpec.BooleanValue harvestMoonEnabled;
//    public static final ForgeConfigSpec.DoubleValue harvestMoonChance;
//    public static final ForgeConfigSpec.IntValue harvestMoonStartNight;
//    public static final ForgeConfigSpec.IntValue harvestMoonInterval;
//    public static final ForgeConfigSpec.IntValue harvestMoonGraceDays;
//    public static final ForgeConfigSpec.BooleanValue starShowersEnabled;
//    public static final ForgeConfigSpec.DoubleValue starShowersChance;
//    public static final ForgeConfigSpec.IntValue starShowersStartNight;
//    public static final ForgeConfigSpec.IntValue starShowersInterval;
//    public static final ForgeConfigSpec.IntValue starShowersGraceDays;
//    public static final ForgeConfigSpec.BooleanValue bloodMoonEnabled;
//    public static final ForgeConfigSpec.DoubleValue bloodMoonChance;
//    public static final ForgeConfigSpec.IntValue bloodMoonStartNight;
//    public static final ForgeConfigSpec.IntValue bloodMoonInterval;
//    public static final ForgeConfigSpec.IntValue bloodMoonGraceDays;
//    public static final ForgeConfigSpec.ConfigValue<Integer[]> lunarWaterTicks;
//    public static final ForgeConfigSpec.DoubleValue meteorChance;
//    public static final ForgeConfigSpec.DoubleValue meteorChanceNight;
//    public static final ForgeConfigSpec.IntValue meteroGateDimension;
//    public static final ForgeConfigSpec.DoubleValue meteorChanceAfterGate;
//    public static final ForgeConfigSpec.DoubleValue meteorChanceAfterGateNight;
//    public static final ForgeConfigSpec.DoubleValue meteorChanceStarShower;
//    public static final ForgeConfigSpec.DoubleValue meteorChanceEnd;
//    public static final ForgeConfigSpec.IntValue meteorSpawnRadius;
//    public static final ForgeConfigSpec.BooleanValue meteors;
//    public static final ForgeConfigSpec.IntValue meteorDisallowRadius;
//    public static final ForgeConfigSpec.IntValue meteorDisallowTime;
//    public static final ForgeConfigSpec.ConfigValue<Set<Integer>> meteorSpawnDimensions;
//    public static final ForgeConfigSpec.BooleanValue meteorCacheEnabled;
//    public static final ForgeConfigSpec.BooleanValue meteorCacheUnloaded;
//    public static final ForgeConfigSpec.ConfigValue<Set<Integer>> enchantingWhitelistDimensions;
//    public static final ForgeConfigSpec.BooleanValue eventNotifications;
    public static final ForgeConfigSpec.ConfigValue<Integer> crystalDurability;
//    public static final ForgeConfigSpec.IntValue hammerDamage;
//    public static final ForgeConfigSpec.DoubleValue bowDamageMultiplier;
//    public static final ForgeConfigSpec.BooleanValue meteorSwordStun;
//    public static final ForgeConfigSpec.DoubleValue meteorSwordExplosionChance;
//    public static final ForgeConfigSpec.ConfigValue<String[]> scytheDropChances;
//
//    public static final Set<ItemStack> scytheDropBlacklist;
//    public static final ForgeConfigSpec.ConfigValue<Set<String>> _scytheDropBlacklist;
//
//    public static final ForgeConfigSpec.ConfigValue<Set<LunarWaterSource>> lunarWaterRemoveNegative;
//    public static final ForgeConfigSpec.ConfigValue<Set<LunarWaterSource>> lunarWaterRemoveAll;

    public static final ForgeConfigSpec.IntValue colorStarShower;



    static {
        BUILDER.push("Enchantments");

        enchantments = BUILDER.comment("If the enchantments should be enabled.")
                        .define("Toggles enchantments", true);

        disallowDayEnchanting = BUILDER.comment("If enchanting should be disallowed during the day")
                .define("Day Enchanting Disabled", true);

        maxLunarEdgeXpMult = BUILDER.comment("The max multiplier on the amount of xp added (which happens during a full moon)\\n\" + \n" +
                        "                \"Can be set to 0 to disable lunar edge xp gains\\n\" + \n" +
                        "                \"The multiplier scales up to the max according to the level and moon phase\\n\" + \n" +
                        "                \"Ex: if the config option is set to 2.5, a full moon with max lunar edge level would give\\n\" + \n" +
                        "                \"3.5x xp and a new moon would give 1x xp\"")
                        .defineInRange("Max Lunar Edge XP Mult", 1.0, 0, 100);

        minLevelLunarEdgeDamage = BUILDER.comment("The amount of additional damage that should be applied to an item with level 1 lunar edge on a full moon.")
                .defineInRange("Min Level Lunar Edge Damage", 1.25, 0, 100);

        maxLevelLunarEdgeDamage = BUILDER.comment("The amount of additional damage that should be applied to an item with max level lunar edge on a full moon.")
                .defineInRange("Min Level Lunar Edge Damage", 3.25, 0, 100);

        baseLunarEdgeDamage = BUILDER.comment("The amount of additional damage that will always be applied regardless of moon phase.")
                        .defineInRange("Base Lunar Edge Damage", 0.0, 0.0, 100);

        BUILDER.pop();

        BUILDER.push("Lunar Water");

        lunarWater = BUILDER.comment("If lunar water should be enabled.")
                        .define("Toggles lunar water", true);

        lunarWaterItemParts = BUILDER.comment("The item that needs to be dropped into a cauldron to turn it into lunar water.\\nExamples include 'minecraft:stick', 'minecraft:wool:3', and 'ore:stone'")
                .defineList(
                        "Lunar Water Items",
                        List.of("minecraft:coal"),
                        o -> o instanceof String
                );

        BUILDER.pop();

        BUILDER.push("Moons");

        BUILDER.push("Full Moon");

        fullMoon = BUILDER.comment("If the vanilla full moon should be considered a proper lunar event")
                .define("Full Moon", true);

        addPotionEffects = BUILDER.comment("If mobs spawned during a full moon should have random potion effects applied to them (similarly to spiders in the base game)")
                .define("Toggles Full Moon mob effects", true);

        additonalMobsChance = BUILDER.comment("The chance for an additional mob to be spawned when a mob spawns during a full moon. The higher the number, the less likely. Set to 0 to disable.")
                .defineInRange("Chances for extra mobs", 5, 0, 1000);

        BUILDER.pop();

        BUILDER.push("Blood Moons");

        bloodMoonSleeping = BUILDER.comment("If sleeping is allowed during a blood moon")
                        .define("Blood Moon Sleeping", false);

        BUILDER.pop();

        BUILDER.push("Star Showers");

        colorStarShower = BUILDER.comment("The hex code of the star shower color")
                        .defineInRange("Star Shower Color", 0xDEC25F, 0x000000, 0xFFFFFF);

        BUILDER.pop();

        BUILDER.pop();

        BUILDER.push("Dimensions");

        allowedDimensions = BUILDER.comment("IDs of the dimensions that lunar events should occur in")
                        .defineList(
                                "Lunar Dimensions",
                                List.of("minecraft:overworld"),
                                o -> o instanceof String
                        );

        BUILDER.pop();

        BUILDER.push("Meteors");

        meteorShardGuardianChance = BUILDER.comment("The chance in percent (1 = 100%) for a meteor shard to be dropped from an elder guardian")
                        .defineInRange("Meteor Shard Guardian Chance", 0.05, 0, 1);

        BUILDER.pop();

        BUILDER.push("Falling Stars");

        fallingStars = BUILDER.comment("If falling stars should be enabled")
                        .define("Falling Stars", true);

        fallingStarRarity = BUILDER.comment("The chance in percent (1 = 100%) for a falling star to appear at night for each player per second")
                        .defineInRange("Falling Star Rarity", 0.01, 0, 1);

        fallingStarRarityShower = BUILDER.comment("The chance for a falling star to appear during a star shower for each player per second")
                        .defineInRange("Falling Star Rarity Shower", 0.15, 0, 1);

        fallingStarImpactVolume = BUILDER.comment("The volume for the falling star impact sound")
                        .defineInRange("Falling Star Impact Volume", 10.0, 0, 30);

        fallingStarAmbientVolume = BUILDER.comment("The volume for the falling star ambient sound")
                        .defineInRange("Falling Star Ambient Volume", 5.0, 0, 20);

        BUILDER.pop();

        BUILDER.push("Blocks");

        BUILDER.push("Crystal");

        crystalDurability = BUILDER.comment("The max durability of the crystal")
                .define("Max crystal durability", 1000);

        BUILDER.pop();

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static Set<ResourceKey<Level>> getAllowedDimensions() {
        return allowedDimensions.get().stream()
                .map(ResourceLocation::new)
                .map(loc -> ResourceKey.create(Registries.DIMENSION, loc))
                .collect(Collectors.toSet());
    }

    public static void refresh() {
        lunarWaterItem = new ArrayList<>(lunarWaterItemParts.get());
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
    }

    public static class LunarEventConfig {
        public boolean enabled;
        public double chance;
        public int startNight;
        public int nightInterval;
        public int graceDays;
    }
}
