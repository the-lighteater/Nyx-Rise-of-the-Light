package dot.lighteater.nyx_rotl.network;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketNyxWorld {

    private final String event;
    private final int skyColor;
    private final float skyModifier;

    public PacketNyxWorld(String event, int skyColor, float skyModifier) {
        this.event = event;
        this.skyColor = skyColor;
        this.skyModifier = skyModifier;
    }

    public static void encode(PacketNyxWorld msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.event == null ? "" : msg.event);
        buf.writeInt(msg.skyColor);
        buf.writeFloat(msg.skyModifier);
    }

    public static PacketNyxWorld decode(FriendlyByteBuf buf) {
        return new PacketNyxWorld(
                buf.readUtf(),
                buf.readInt(),
                buf.readFloat()
        );
    }

    public static void handle(PacketNyxWorld msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NyxWorld.clientCurrentEvent =
                    msg.event.isEmpty() ? null : msg.event;

            NyxWorld.clientEventSkyColor = msg.skyColor;
            NyxWorld.clientEventSkyModifier = msg.skyModifier;
        });

        NyxROTL.LOGGER.info(
                "Client received celestial event: {}",
                msg.event
        );

        ctx.get().setPacketHandled(true);
    }
}
