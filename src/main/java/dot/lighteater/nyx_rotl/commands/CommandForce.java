package dot.lighteater.nyx_rotl.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.lunarevents.CelestialEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandForce {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(
                Commands.literal("nyxforce")
                        .requires(source -> source.hasPermission(2))

                        // /nyxforce <event>
                        .then(
                                Commands.argument(
                                                "event",
                                                StringArgumentType.word()
                                        )
                                        .suggests((context, builder) -> {

                                            ServerPlayer player =
                                                    context.getSource()
                                                            .getPlayerOrException();

                                            NyxWorld nyx =
                                                    NyxWorld.get(
                                                            player.serverLevel()
                                                    );

                                            for (CelestialEvent event :
                                                    nyx.events) {

                                                builder.suggest(
                                                        event.name
                                                );
                                            }

                                            builder.suggest("clear");

                                            return builder.buildFuture();
                                        })

                                        .executes(context -> {

                                            ServerPlayer player =
                                                    context.getSource()
                                                            .getPlayerOrException();

                                            String eventName =
                                                    StringArgumentType.getString(
                                                            context,
                                                            "event"
                                                    );

                                            NyxWorld nyx =
                                                    NyxWorld.get(
                                                            player.serverLevel()
                                                    );

                                            if (eventName.equalsIgnoreCase("clear")) {

                                                nyx.forcedEvent = null;
                                                nyx.setDirty();

                                                player.sendSystemMessage(
                                                        Component.literal(
                                                                "[Nyx] Forced event cleared."
                                                        )
                                                );

                                                return 1;
                                            }

                                            CelestialEvent event =
                                                    nyx.events.stream()
                                                            .filter(e ->
                                                                    e.name.equalsIgnoreCase(
                                                                            eventName
                                                                    ))
                                                            .findFirst()
                                                            .orElse(null);

                                            if (event == null) {

                                                player.sendSystemMessage(
                                                        Component.literal(
                                                                "[Nyx] Unknown event: "
                                                                        + eventName
                                                        )
                                                );

                                                return 0;
                                            }

                                            nyx.forcedEvent =
                                                    event.name;

                                            nyx.setDirty();

                                            player.sendSystemMessage(
                                                    Component.literal(
                                                            "[Nyx] Forced event set to: "
                                                                    + event.name
                                                    )
                                            );

                                            return 1;
                                        })
                        )
        );
    }
}