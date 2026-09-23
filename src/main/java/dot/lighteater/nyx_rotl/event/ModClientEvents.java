package dot.lighteater.nyx_rotl.event;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.lunarevents.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ModClientEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    private static String lastClientEvent = null;

    /**
     * Client-side copies of the lunar event definitions.
     *
     * The server has the authoritative NyxWorld SavedData, but the
     * client does not need its own SavedData instance just to render
     * the current lunar event.
     *
     * Instead, PacketNyxWorld gives us the event name and we resolve
     * that name to a LunarEvent here.
     */
    private static final List<CelestialEvent> CLIENT_EVENTS = List.of(
            // Lunar Events
            new StarShower(),
            new BloodMoon(),
            new FullMoon(),
            new HarvestMoon(),

            // Solar Events
            new SolarEclipse()
    );

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            return;
        }

        CelestialEvent currentEvent = getCurrentClientEvent();

        String currentEventName = NyxWorld.clientCurrentEvent;

        if (!Objects.equals(currentEventName, lastClientEvent)) {

            NyxROTL.LOGGER.debug(
                    "[ClientEvent] Event changed: {} -> {}",
                    lastClientEvent,
                    currentEventName
            );

            if (currentEvent instanceof SolarEclipse eclipse) {
                eclipse.reset();

                NyxROTL.LOGGER.debug(
                        "[ClientEvent] Solar eclipse timer reset"
                );
            }

            lastClientEvent = currentEventName;
        }

        if (currentEvent != null) {
            currentEvent.tick(
                    mc.level,
                    mc.level.isDay()
            );
        }
    }

    // ============================================================
    // DEBUG OVERLAY
    // ============================================================

    @SubscribeEvent
    public static void onDebug(CustomizeGuiOverlayEvent.DebugText event) {

        Minecraft mc = Minecraft.getInstance();

        if (!mc.options.renderDebug) {
            return;
        }

        event.getLeft().add("");

        String name = dot.lighteater.nyx_rotl.capabilities.NyxWorld.clientCurrentEvent == null
                ? "None"
                : dot.lighteater.nyx_rotl.capabilities.NyxWorld.clientCurrentEvent;

        event.getLeft().add(
                "[" + NyxROTL.MODID + "] CurrEvent: " + name
        );
    }

    // ============================================================
    // CLIENT EVENT LOOKUP
    // ============================================================

    /**
     * Finds the LunarEvent definition corresponding to the event
     * name received from the server.
     */
    public static CelestialEvent getCurrentClientEvent() {

        String currentEvent = NyxWorld.clientCurrentEvent;

        if (currentEvent == null) {
            return null;
        }

        return CLIENT_EVENTS.stream()
                .filter(event -> event.name.equals(currentEvent))
                .findFirst()
                .orElse(null);
    }

    // ============================================================
    // FOG COLOR
    // ============================================================

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.ComputeFogColor event) {

        if (!Config.moonEventTint.get()) {
            return;
        }

        int color = NyxWorld.clientEventSkyColor;

        if (color == 0) {
            return;
        }

        float modifier = NyxWorld.clientEventSkyModifier;

        event.setRed(
                lerp(
                        event.getRed(),
                        ((color >> 16) & 255) / 255F,
                        modifier
                )
        );

        event.setGreen(
                lerp(
                        event.getGreen(),
                        ((color >> 8) & 255) / 255F,
                        modifier
                )
        );

        event.setBlue(
                lerp(
                        event.getBlue(),
                        (color & 255) / 255F,
                        modifier
                )
        );
    }

    // ============================================================
    // UTILITIES
    // ============================================================

    private static float lerp(
            float start,
            float end,
            float amount
    ) {
        return start + (end - start) * amount;
    }
}