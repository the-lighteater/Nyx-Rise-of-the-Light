package dot.lighteater.nyx_rotl.datagen;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

// Item Model Generation
// Makes the item models with runData.
// Code credit goes to Kaupenjoe for the simpleItem method and in general this file.

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NyxROTL.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.FALLEN_STAR);

        simpleItem(ModItems.LUNAR_WATER_BOTTLE);

        simpleItem(ModItems.LUNAR_WATER_BUCKET);

        simpleItem(ModItems.METEOR_DUST);
        simpleItem(ModItems.METEOR_INGOT);
        simpleItem(ModItems.METEOR_SHARD);

        simpleItem(ModItems.UNREFINED_CRYSTAL);

        bowItem(ModItems.METEOR_BOW);

        handheldItem(ModItems.METEOR_SWORD);
        handheldItem(ModItems.METEOR_AXE);
        handheldItem(ModItems.METEOR_PICKAXE);
        handheldItem(ModItems.METEOR_HOE);
        handheldItem(ModItems.METEOR_SHOVEL);

        trimmedArmorItem(ModItems.METEOR_HELMET);
        trimmedArmorItem(ModItems.METEOR_CHESTPLATE);
        trimmedArmorItem(ModItems.METEOR_LEGGINGS);
        trimmedArmorItem(ModItems.METEOR_BOOTS);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(NyxROTL.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(NyxROTL.MODID,"item/" + item.getId().getPath()));
    }

    // Shoutout to El_Redstoniano for making this
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = NyxROTL.MODID; // Change this to your mod id

        if(itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.entrySet().forEach(entry -> {

                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    private void bowItem(RegistryObject<Item> item) {
        String name = item.getId().getPath();

        // Base bow model
        ModelFile baseModel = withExistingParent(
                name,
                mcLoc("item/bow")
        ).texture(
                "layer0",
                modLoc("item/" + name)
        );

        // Pulling stage 0
        ModelFile pulling0 = withExistingParent(
                name + "_pulling_0",
                mcLoc("item/bow_pulling_0")
        ).texture(
                "layer0",
                modLoc("item/" + name + "_pulling_0")
        );

        // Pulling stage 1
        ModelFile pulling1 = withExistingParent(
                name + "_pulling_1",
                mcLoc("item/bow_pulling_1")
        ).texture(
                "layer0",
                modLoc("item/" + name + "_pulling_1")
        );

        // Pulling stage 2
        ModelFile pulling2 = withExistingParent(
                name + "_pulling_2",
                mcLoc("item/bow_pulling_2")
        ).texture(
                "layer0",
                modLoc("item/" + name + "_pulling_2")
        );

        // Main bow model with pulling predicates
        getBuilder(name)
                .parent(baseModel)
                .override()
                .predicate(mcLoc("pulling"), 1.0F)
                .predicate(mcLoc("pull"), 0.65F)
                .model(pulling0)
                .end()
                .override()
                .predicate(mcLoc("pulling"), 1.0F)
                .predicate(mcLoc("pull"), 0.9F)
                .model(pulling1)
                .end()
                .override()
                .predicate(mcLoc("pulling"), 1.0F)
                .predicate(mcLoc("pull"), 1.0F)
                .model(pulling2)
                .end();
    }

}
