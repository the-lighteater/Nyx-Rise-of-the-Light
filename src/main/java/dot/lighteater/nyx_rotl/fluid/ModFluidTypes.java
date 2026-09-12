package dot.lighteater.nyx_rotl.fluid;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, NyxROTL.MODID);
    public static final RegistryObject<FluidType> LUNAR_WATER_TYPE = REGISTRY.register("lunar_water", LunarWaterFluidType::new);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}