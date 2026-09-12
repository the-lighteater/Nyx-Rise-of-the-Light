package dot.lighteater.nyx_rotl.registry;

import dot.lighteater.nyx_rotl.commands.CommandMeteor;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandMeteor.register(event.getDispatcher());
    }
}
