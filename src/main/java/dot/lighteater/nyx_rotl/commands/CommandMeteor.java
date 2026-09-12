package dot.lighteater.nyx_rotl.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dot.lighteater.nyx_rotl.entities.FallingMeteor;
import dot.lighteater.nyx_rotl.registry.ModEntities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class CommandMeteor {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("nyxmeteor")
                .requires(source -> source.hasPermission(2))

                // /nyxmeteor
                .executes(ctx -> spawnMeteor(ctx.getSource(), null, null, null, false))

                // /nyxmeteor x z
                .then(Commands.argument("x", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("z", DoubleArgumentType.doubleArg())

                                // /nyxmeteor x z
                                .executes(ctx -> spawnMeteor(
                                        ctx.getSource(),
                                        DoubleArgumentType.getDouble(ctx, "x"),
                                        DoubleArgumentType.getDouble(ctx, "z"),
                                        null,
                                        false
                                ))

                                // /nyxmeteor x z size
                                .then(Commands.argument("size", IntegerArgumentType.integer(1))
                                        .executes(ctx -> spawnMeteor(
                                                ctx.getSource(),
                                                DoubleArgumentType.getDouble(ctx, "x"),
                                                DoubleArgumentType.getDouble(ctx, "z"),
                                                IntegerArgumentType.getInteger(ctx, "size"),
                                                false
                                        ))

                                        // /nyxmeteor x z size homing
                                        .then(Commands.argument("homing", BoolArgumentType.bool())
                                                .executes(ctx -> spawnMeteor(
                                                        ctx.getSource(),
                                                        DoubleArgumentType.getDouble(ctx, "x"),
                                                        DoubleArgumentType.getDouble(ctx, "z"),
                                                        IntegerArgumentType.getInteger(ctx, "size"),
                                                        BoolArgumentType.getBool(ctx, "homing")
                                                ))
                                        )
                                )
                        )
                )
        );
    }

    private static int spawnMeteor(CommandSourceStack source,
                                   Double xArg,
                                   Double zArg,
                                   Integer size,
                                   boolean homing) {

        ServerLevel level = source.getLevel();

        double x = xArg != null ? xArg : source.getPosition().x;
        double z = zArg != null ? zArg : source.getPosition().z;

        BlockPos pos = new BlockPos((int)x, 0, (int)z);

        FallingMeteor meteor = FallingMeteor.spawn(level, pos, ModEntities.FALLING_METEOR.get());

        if (size != null) {
            meteor.setSize(size);
        }

        meteor.homing = homing;

        BlockPos finalPos = meteor.blockPosition();

        source.sendSuccess(() -> Component.literal(
                "Meteor spawned at " + finalPos.getX() + ", " + finalPos.getY() + ", " + finalPos.getZ()
        ), true);

        return 1;
    }
}