package dot.lighteater.nyx_rotl.item;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.fluid.ModFluids;
import dot.lighteater.nyx_rotl.item.custom.FallenStar;
import dot.lighteater.nyx_rotl.item.custom.LunarWaterBottle;
import dot.lighteater.nyx_rotl.item.custom.LunarWaterBucket;
import dot.lighteater.nyx_rotl.item.custom.MeteorAxe;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NyxROTL.MODID);

    public static final RegistryObject<Item> METEOR_AXE = ITEMS.register("meteor_axe",
            () -> new MeteorAxe(Tiers.IRON, 5, .4f, new Item.Properties()));

    public static final RegistryObject<Item> FALLEN_STAR = ITEMS.register("fallen_star",
            () -> new FallenStar(new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_WATER_BUCKET = ITEMS.register("lunar_water_bucket",
            () -> new LunarWaterBucket(ModFluids.LUNAR_WATER.get(), new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_WATER_BOTTLE = ITEMS.register("lunar_water_bottle",
            LunarWaterBottle::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
