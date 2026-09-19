package dot.lighteater.nyx_rotl.event;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.entities.MeteorKat;
import dot.lighteater.nyx_rotl.registry.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NyxROTL.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.METEOR_KAT.get(), MeteorKat.createAttributes().build());
    }
}