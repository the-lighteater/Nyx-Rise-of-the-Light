package dot.lighteater.nyx_rotl.registry;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.entities.FallingMeteor;
import dot.lighteater.nyx_rotl.entities.FallingStar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, NyxROTL.MODID);

    public static final RegistryObject<EntityType<FallingStar>> FALLING_STAR =
            ENTITY_TYPES.register("falling_star", () -> EntityType.Builder.of(FallingStar::new, MobCategory.CREATURE)
                    .sized(2.5f, 2.5f).build("falling_star"));

    public static final RegistryObject<EntityType<FallingMeteor>> FALLING_METEOR =
            ENTITY_TYPES.register("falling_meteor", () -> EntityType.Builder.of(FallingMeteor::new, MobCategory.CREATURE)
                    .sized(2.5f, 2.5f).build("falling_meteor"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}