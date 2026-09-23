package dot.lighteater.nyx_rotl.network;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class PacketNyxWorld {

    private final String event;
    private final int skyColor;
    private final float skyModifier;

    public final Set<BlockPos> meteorLandingSites;
    public final Set<BlockPos> cachedMeteorPositions;

    public PacketNyxWorld(
            String event,
            int skyColor,
            float skyModifier,
            Set<BlockPos> meteorLandingSites,
            Set<BlockPos> cachedMeteorPositions
    ) {
        this.event = event;
        this.skyColor = skyColor;
        this.skyModifier = skyModifier;
        this.meteorLandingSites = meteorLandingSites;
        this.cachedMeteorPositions = cachedMeteorPositions;
    }

    public static void encode(
            PacketNyxWorld msg,
            FriendlyByteBuf buf
    ) {
        // Celestial event data
        buf.writeUtf(msg.event == null ? "" : msg.event);
        buf.writeInt(msg.skyColor);
        buf.writeFloat(msg.skyModifier);

        // Meteor landing sites
        buf.writeInt(msg.meteorLandingSites.size());

        for (BlockPos pos : msg.meteorLandingSites) {
            buf.writeLong(pos.asLong());
        }

        // Cached meteor positions
        buf.writeInt(msg.cachedMeteorPositions.size());

        for (BlockPos pos : msg.cachedMeteorPositions) {
            buf.writeLong(pos.asLong());
        }
    }

    public static PacketNyxWorld decode(
            FriendlyByteBuf buf
    ) {
        String event = buf.readUtf();
        int skyColor = buf.readInt();
        float skyModifier = buf.readFloat();

        // Meteor landing sites
        int landingSiteCount = buf.readInt();

        Set<BlockPos> meteorLandingSites = new HashSet<>();

        for (int i = 0; i < landingSiteCount; i++) {
            meteorLandingSites.add(
                    BlockPos.of(buf.readLong())
            );
        }

        // Cached meteor positions
        int cachedMeteorCount = buf.readInt();

        Set<BlockPos> cachedMeteorPositions = new HashSet<>();

        for (int i = 0; i < cachedMeteorCount; i++) {
            cachedMeteorPositions.add(
                    BlockPos.of(buf.readLong())
            );
        }

        return new PacketNyxWorld(
                event,
                skyColor,
                skyModifier,
                meteorLandingSites,
                cachedMeteorPositions
        );
    }

    public static void handle(
            PacketNyxWorld msg,
            Supplier<NetworkEvent.Context> ctx
    ) {
        ctx.get().enqueueWork(() -> {

            // Celestial event data
            NyxWorld.clientCurrentEvent =
                    msg.event.isEmpty()
                            ? null
                            : msg.event;

            NyxWorld.clientEventSkyColor =
                    msg.skyColor;

            NyxWorld.clientEventSkyModifier =
                    msg.skyModifier;

            // Debug celestial event data
            NyxROTL.LOGGER.debug(
                    "Client received currentEvent: {}",
                    NyxWorld.clientCurrentEvent
            );

            NyxROTL.LOGGER.debug(
                    "Client received eventSkyColor: {}",
                    NyxWorld.clientEventSkyColor
            );

            NyxROTL.LOGGER.debug(
                    "Client received eventSkyModifier: {}",
                    NyxWorld.clientEventSkyModifier
            );

            // Meteor data
            NyxWorld.clientMeteorLandingSites.clear();
            NyxWorld.clientMeteorLandingSites.addAll(
                    msg.meteorLandingSites
            );

            NyxWorld.clientCachedMeteorPositions.clear();
            NyxWorld.clientCachedMeteorPositions.addAll(
                    msg.cachedMeteorPositions
            );
        });

        NyxROTL.LOGGER.info(
                "Client received celestial event: {}",
                msg.event
        );

        ctx.get().setPacketHandled(true);
    }
}