package dot.lighteater.nyx_rotl.capabilities;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.lunarevents.LunarEvent;
import dot.lighteater.nyx_rotl.lunarevents.StarShower;
import dot.lighteater.nyx_rotl.network.PacketHandler;
import dot.lighteater.nyx_rotl.network.PacketNyxWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;


public class NyxWorld extends SavedData {

    public static final String NAME = "nyx_world";

    public final List<LunarEvent> events = new ArrayList<>();

    public String currentEvent = null;
    public boolean wasDaytime = false;

    public boolean init = false;

    public float eventSkyModifier = 0f;
    public int eventSkyColor = 0;

    public static String clientCurrentEvent = null;
    public static int clientEventSkyColor = 0;
    public static float clientEventSkyModifier = 0f;

    public final Set<BlockPos> cachedMeteorPositions = new HashSet<>();
    public final Set<BlockPos> meteorLandingSites = new HashSet<>();
    public final Map<ChunkPos, Integer> playersPresentTicks = new HashMap<>();

    // ✅ FIX: must store ResourceLocation, not ResourceKey
    public final Set<ResourceLocation> visitedDimensions = new HashSet<>();

    public NyxWorld() {
        events.add(new StarShower());
    }

    // -----------------------------------
    // ACCESSOR (1.20.1 correct)
    // -----------------------------------
    public static NyxWorld get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                NyxWorld::load,
                NyxWorld::new,
                NAME
        );
    }

    // -----------------------------------
    // SAVE
    // -----------------------------------
    @Override
    public CompoundTag save(CompoundTag tag) {

        if (currentEvent != null) {
            tag.putString("event", currentEvent);
        }

        tag.putBoolean("was_daytime", wasDaytime);
        tag.putFloat("sky_mod", eventSkyModifier);
        tag.putInt("sky_color", eventSkyColor);

        ListTag landings = new ListTag();
        for (BlockPos pos : meteorLandingSites) {
            landings.add(LongTag.valueOf(pos.asLong()));
        }
        tag.put("meteor_landings", landings);

        ListTag meteors = new ListTag();
        for (BlockPos pos : cachedMeteorPositions) {
            meteors.add(LongTag.valueOf(pos.asLong()));
        }
        tag.put("cached_meteors", meteors);

        ListTag chunks = new ListTag();
        for (Map.Entry<ChunkPos, Integer> e : playersPresentTicks.entrySet()) {
            CompoundTag c = new CompoundTag();
            c.putInt("x", e.getKey().x);
            c.putInt("z", e.getKey().z);
            c.putInt("ticks", e.getValue());
            chunks.add(c);
        }
        tag.put("players_present_ticks", chunks);

        // ✅ FIX: store as string
        ListTag dims = new ListTag();
        for (ResourceLocation id : visitedDimensions) {
            dims.add(StringTag.valueOf(id.toString()));
        }
        tag.put("visited_dims", dims);

        return tag;
    }

    // -----------------------------------
    // LOAD
    // -----------------------------------
    public static NyxWorld load(CompoundTag tag) {

        NyxWorld data = new NyxWorld();

        data.currentEvent = tag.contains("event") ? tag.getString("event") : null;
        data.wasDaytime = tag.getBoolean("was_daytime");
        data.eventSkyModifier = tag.getFloat("sky_mod");
        data.eventSkyColor = tag.getInt("sky_color");

        for (Tag t : tag.getList("meteor_landings", Tag.TAG_LONG)) {
            data.meteorLandingSites.add(BlockPos.of(((LongTag) t).getAsLong()));
        }

        for (Tag t : tag.getList("cached_meteors", Tag.TAG_LONG)) {
            data.cachedMeteorPositions.add(BlockPos.of(((LongTag) t).getAsLong()));
        }

        for (Tag t : tag.getList("players_present_ticks", Tag.TAG_COMPOUND)) {
            CompoundTag c = (CompoundTag) t;
            data.playersPresentTicks.put(
                    new ChunkPos(c.getInt("x"), c.getInt("z")),
                    c.getInt("ticks")
            );
        }

        // ✅ FIX: read strings properly
        for (Tag t : tag.getList("visited_dims", Tag.TAG_STRING)) {
            String s = ((StringTag) t).getAsString();
            data.visitedDimensions.add(new ResourceLocation(s));
        }

        return data;
    }

    // -----------------------------------
    // TICK
    // -----------------------------------
    public void tick(ServerLevel level) {

        boolean isDay = level.isDay();

        updateVisitedDimensions(level);
        updatePlayerPresence(level);

        if (!init) {
            wasDaytime = isDay;
            init = true;
            return;
        }

        boolean lastDay = wasDaytime;
        wasDaytime = isDay;

        // Give events their tick
        for (LunarEvent event : events) {
            event.tick(level, lastDay);
        }

        // Start an event
        if (currentEvent == null) {

            for (LunarEvent event : events) {

                if (event.shouldStart(level, lastDay)) {

                    currentEvent = event.name;
                    eventSkyColor = event.getSkyColor();

                    NyxROTL.LOGGER.info(
                            "Lunar event started: {}",
                            currentEvent
                    );

                    sendToClients();
                    setDirty();

                    break;
                }
            }
        }

        // Stop current event
        if (currentEvent != null) {

            LunarEvent active = events.stream()
                    .filter(event -> event.name.equals(currentEvent))
                    .findFirst()
                    .orElse(null);

            if (active != null && active.shouldStop(level, lastDay)) {

                NyxROTL.LOGGER.info(
                        "Lunar event stopped: {}",
                        currentEvent
                );

                currentEvent = null;
                eventSkyColor = 0;

                sendToClients();
                setDirty();
            }
        }
    }

    private void sendToClients() {
        PacketHandler.sendToAll(
                new PacketNyxWorld(
                        currentEvent,
                        eventSkyColor,
                        eventSkyModifier
                )
        );
    }

    private void updateVisitedDimensions(ServerLevel level) {
        if (level.getGameTime() % 200 != 0) {
            return;
        }

        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            visitedDimensions.add(
                    player.level().dimension().location()
            );
        }
    }

    private void updatePlayerPresence(ServerLevel level) {
        if (!Config.meteors.get()) {
            return;
        }

        if (level.getGameTime() % 100 != 0) {
            return;
        }

        Set<ChunkPos> remaining = new HashSet<>(
                playersPresentTicks.keySet()
        );

        int radius = Config.meteorDisallowRadius.get();

        for (Player player : level.players()) {

            ChunkPos center = player.chunkPosition();

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    ChunkPos pos = new ChunkPos(
                            center.x + x,
                            center.z + z
                    );

                    playersPresentTicks.merge(
                            pos,
                            100,
                            Integer::sum
                    );

                    remaining.remove(pos);
                }
            }
        }

        // Chunks no longer near players lose 100 ticks of presence.
        for (ChunkPos pos : remaining) {

            int time = playersPresentTicks.get(pos) - 100;

            if (time <= 0) {
                playersPresentTicks.remove(pos);
            } else {
                playersPresentTicks.put(pos, time);
            }
        }
    }
}



//    public NyxWorld() {
////        events.add(new HarvestMoon());
//        events.add(new StarShower());
////        events.add(new BloodMoon());
////        events.add(new FullMoon());
//    }



        // ☄️ meteor chunk tracking
//        if (Config.meteors.get() && time % 100 == 0) {
//
//            data.playersPresentTicks.clear();
//
//            for (Player player : level.players()) {
//
//                ChunkPos center = player.chunkPosition();
//
//                for (int x = -Config.meteorDisallowRadius.get(); x <= Config.meteorDisallowRadius.get(); x++) {
//                    for (int z = -Config.meteorDisallowRadius.get(); z <= Config.meteorDisallowRadius.get(); z++) {
//
//                        ChunkPos pos = new ChunkPos(center.x + x, center.z + z);
//                        data.playersPresentTicks.merge(pos, 100, Integer::sum);
//                    }
//                }
//            }
//        }

