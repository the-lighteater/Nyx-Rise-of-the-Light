package dot.lighteater.nyx_rotl.registry;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, NyxROTL.MODID);

    public static final RegistryObject<SoundEvent> lunarWaterSound = registerSoundEvents("lunar_water");
    public static final RegistryObject<SoundEvent> fallingStarSound = registerSoundEvents("falling_star");
    public static final RegistryObject<SoundEvent> fallingStarImpactSound = registerSoundEvents("falling_star_impact");
    public static final RegistryObject<SoundEvent> fallingMeteorSound = registerSoundEvents("falling_meteor");
    public static final RegistryObject<SoundEvent> fallingMeteorImpactSound = registerSoundEvents("falling_meteor_impact");
    public static final RegistryObject<SoundEvent> hammerStartSound = registerSoundEvents("hammer_start");
    public static final RegistryObject<SoundEvent> hammerEndSound = registerSoundEvents("hammer_end");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(NyxROTL.MODID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}