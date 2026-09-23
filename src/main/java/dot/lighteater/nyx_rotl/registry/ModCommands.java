package dot.lighteater.nyx_rotl.registry;

import com.mojang.brigadier.CommandDispatcher;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.commands.CommandForce;
import dot.lighteater.nyx_rotl.commands.CommandMeteor;
import dot.lighteater.nyx_rotl.network.PacketHandler;
import dot.lighteater.nyx_rotl.network.PacketNyxWorld;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandMeteor.register(event.getDispatcher());
        CommandForce.register(event.getDispatcher());

        CommandDispatcher<CommandSourceStack> dispatcher =
                event.getDispatcher();

        dispatcher.register(
                Commands.literal("syncToClient")
                        .requires(source -> source.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player =
                                    context.getSource().getPlayerOrException();

                            NyxWorld nyx =
                                    NyxWorld.get(player.serverLevel());

                            // Debug: show exactly what is being sent.
                            player.sendSystemMessage(
                                    Component.literal(
                                            "[Nyx] currentEvent = "
                                                    + nyx.currentEvent
                                    ).withStyle(ChatFormatting.AQUA)
                            );

                            player.sendSystemMessage(
                                    Component.literal(
                                            "[Nyx] eventSkyColor = "
                                                    + nyx.eventSkyColor
                                    ).withStyle(ChatFormatting.AQUA)
                            );

                            player.sendSystemMessage(
                                    Component.literal(
                                            "[Nyx] eventSkyModifier = "
                                                    + nyx.eventSkyModifier
                                    ).withStyle(ChatFormatting.AQUA)
                            );

                            player.sendSystemMessage(
                                    Component.literal(
                                            "[Nyx] meteorLandingSites = "
                                                    + nyx.meteorLandingSites.size()
                                    ).withStyle(ChatFormatting.AQUA)
                            );

                            player.sendSystemMessage(
                                    Component.literal(
                                            "[Nyx] cachedMeteorPositions = "
                                                    + nyx.cachedMeteorPositions.size()
                                    ).withStyle(ChatFormatting.AQUA)
                            );

                            PacketNyxWorld packet = new PacketNyxWorld(
                                    nyx.currentEvent,
                                    nyx.eventSkyColor,
                                    nyx.eventSkyModifier,
                                    nyx.meteorLandingSites,
                                    nyx.cachedMeteorPositions
                            );

                            PacketHandler.sendToPlayer(player, packet);

                            player.sendSystemMessage(
                                    Component.literal(
                                            "[Nyx] PacketNyxWorld sent."
                                    ).withStyle(ChatFormatting.GREEN)
                            );

                            return 1;
                        })
        );
    }
}