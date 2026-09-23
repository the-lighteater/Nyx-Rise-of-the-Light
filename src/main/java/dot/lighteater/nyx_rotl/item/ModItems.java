package dot.lighteater.nyx_rotl.item;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.fluid.ModFluids;
import dot.lighteater.nyx_rotl.item.custom.*;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NyxROTL.MODID);


    public static final RegistryObject<Item> FALLEN_STAR = ITEMS.register("fallen_star",
            () -> new FallenStar(new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_WATER_BUCKET = ITEMS.register("lunar_water_bucket",
            () -> new LunarWaterBucket(ModFluids.LUNAR_WATER.get(), new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_WATER_BOTTLE = ITEMS.register("lunar_water_bottle",
            LunarWaterBottle::new);

    public static final RegistryObject<Item> METEOR_SHARD = ITEMS.register("meteor_shard",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METEOR_INGOT = ITEMS.register("meteor_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METEOR_DUST = ITEMS.register("meteor_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> UNREFINED_CRYSTAL = ITEMS.register("unrefined_crystal",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METEOR_FINDER = ITEMS.register("meteor_finder",
            () -> new MeteorFinder(new Item.Properties()));

    public static final RegistryObject<Item> METEOR_HAMMER = ITEMS.register("meteor_hammer",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SCYTHE = ITEMS.register("scythe",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METEOR_BOW = ITEMS.register("meteor_bow",
            () -> new BowItem(new Item.Properties()));

    public static final RegistryObject<Item> METEOR_AXE = ITEMS.register("meteor_axe",
            () -> new MeteorAxe(ModToolTiers.METEOR, 4, -3f, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_PICKAXE = ITEMS.register("meteor_pickaxe",
            () -> new PickaxeItem(ModToolTiers.METEOR, 0, -2.8f, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_SHOVEL = ITEMS.register("meteor_shovel",
            () -> new ShovelItem(ModToolTiers.METEOR, 0, -3f, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_HOE = ITEMS.register("meteor_hoe",
            () -> new HoeItem(ModToolTiers.METEOR, 0, 0f, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_SWORD = ITEMS.register("meteor_sword",
            () -> new SwordItem(ModToolTiers.METEOR, 2, -2.4f, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_HELMET = ITEMS.register("meteor_helmet",
            () -> new ArmorItem(ModArmorMaterials.METEOR, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_CHESTPLATE = ITEMS.register("meteor_chestplate",
            () -> new ArmorItem(ModArmorMaterials.METEOR, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_LEGGINGS = ITEMS.register("meteor_leggings",
            () -> new ArmorItem(ModArmorMaterials.METEOR, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> METEOR_BOOTS = ITEMS.register("meteor_boots",
            () -> new ArmorItem(ModArmorMaterials.METEOR, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
