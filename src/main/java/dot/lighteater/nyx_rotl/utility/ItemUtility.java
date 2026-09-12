package dot.lighteater.nyx_rotl.utility;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;

public class ItemUtility {
    private static final ResourceLocation[] ADDITIONAL_WEAPON_TAGS = new ResourceLocation[] {
            new ResourceLocation("forge", "swords"),
            new ResourceLocation("forge", "tools/swords"),
            new ResourceLocation("forge", "tools/axe"),
            new ResourceLocation("forge", "tools/pickaxes"),
            new ResourceLocation("forge", "tools/trident"),

            new ResourceLocation("forge", "knives"),
            new ResourceLocation("forge", "tools/knives"),

            new ResourceLocation("forge", "spears"),
            new ResourceLocation("forge", "tools/spears"),

            new ResourceLocation("forge", "maces"),
            new ResourceLocation("forge", "tools/maces"),

            new ResourceLocation("forge", "hammers"),
            new ResourceLocation("forge", "tools/hammers"),

            new ResourceLocation("forge", "battleaxes"),
            new ResourceLocation("forge", "tools/battleaxes"),

            new ResourceLocation("forge", "throwing_weapons"),
            new ResourceLocation("upgrade_scrolls", "weapon_whitelist")
    };

    private static final ResourceLocation[] CURIOS_TAGS = new ResourceLocation[] {
            new ResourceLocation("curios", "head"),
            new ResourceLocation("upgrade_scrolls", "curio_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_BOW_TAGS = new ResourceLocation[] {
            new ResourceLocation("forge", "tools/bows"),
            new ResourceLocation("forge", "crossbows"),
            new ResourceLocation("upgrade_scrolls", "bow_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_SHIELD_TAGS = new ResourceLocation[] {
                new ResourceLocation("forge", "tools/shields"),
            new ResourceLocation("upgrade_scrolls", "shield_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_SPAWNER_TAGS = new ResourceLocation[] {
            new ResourceLocation("upgrade_scrolls", "spawner_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_HELMET_TAGS = new ResourceLocation[] {
            new ResourceLocation("forge:helmets"),
            new ResourceLocation("forge:armors/helmets"),
            new ResourceLocation("upgrade_scrolls", "helmet_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_CHESTPLATE_TAGS = new ResourceLocation[] {
            new ResourceLocation("forge:chestplates"),
            new ResourceLocation("forge:armors/chestplates"),
            new ResourceLocation("upgrade_scrolls", "chestplate_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_LEGGINGS_TAGS = new ResourceLocation[] {
            new ResourceLocation("forge:leggings"),
            new ResourceLocation("forge:armors/leggings"),
            new ResourceLocation("upgrade_scrolls", "leggings_whitelist")
    };

    private static final ResourceLocation[] ADDITIONAL_BOOTS_TAGS = new ResourceLocation[] {
            new ResourceLocation("forge:boots"),
            new ResourceLocation("forge:armors/boots"),
            new ResourceLocation("upgrade_scrolls", "boots_whitelist")
    };

    public static int isValidWeaponOrCurio(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof net.minecraft.world.item.SwordItem ||
                item instanceof net.minecraft.world.item.AxeItem ||
                item instanceof net.minecraft.world.item.PickaxeItem ||
                item instanceof net.minecraft.world.item.ShovelItem ||
                item instanceof net.minecraft.world.item.HoeItem ||
                item instanceof net.minecraft.world.item.ShieldItem ||
                item instanceof net.minecraft.world.item.BowItem ||
                item instanceof net.minecraft.world.item.CrossbowItem ||
                item instanceof net.minecraft.world.item.TridentItem) {
            return 1;
        }

        for (ResourceLocation rl : ADDITIONAL_WEAPON_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 1;
            }
        }

        for (ResourceLocation rl : CURIOS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 2;
            }
        }

        for (ResourceLocation rl : ADDITIONAL_SHIELD_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 1;
            }
        }

        for (ResourceLocation rl : ADDITIONAL_BOW_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return 1;
            }
        }


        if (ModList.get().isLoaded("spartanweaponry")) {
            try {
                Class<?> spartanItemClass = Class.forName("net.spartanweaponry.item.SpartanWeaponItem");
                if (spartanItemClass.isInstance(item)) {
                    return 1;
                }

                Class<?> throwableItemClass = Class.forName("net.spartanweaponry.item.ThrowingWeaponItem");
                if (throwableItemClass.isInstance(item)) {

                    return 1;
                }
            } catch (ClassNotFoundException ignored) {}
        }

        if (ModList.get().isLoaded("curios")) {
            try {
                Class<?> curioClass = Class.forName("top.theillusivec4.curios.api.type.capability.ICurioItem");
                if (curioClass.isInstance(item)) {
                    return 2;
                }
            } catch (ClassNotFoundException ignored) {}
        }


        return 0;
    }

    public static boolean isValidWeapon(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof net.minecraft.world.item.SwordItem ||
                item instanceof net.minecraft.world.item.AxeItem ||
                item instanceof net.minecraft.world.item.PickaxeItem ||
                item instanceof net.minecraft.world.item.ShovelItem ||
                item instanceof net.minecraft.world.item.HoeItem ||
                item instanceof net.minecraft.world.item.TridentItem) {
            return true;
        }

        for (ResourceLocation rl : ADDITIONAL_WEAPON_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }

        if (ModList.get().isLoaded("spartanweaponry")) {
            try {
                Class<?> spartanItemClass = Class.forName("net.spartanweaponry.item.SpartanWeaponItem");
                if (spartanItemClass.isInstance(item)) {
                    return true;
                }

                Class<?> throwableItemClass = Class.forName("net.spartanweaponry.item.ThrowingWeaponItem");
                if (throwableItemClass.isInstance(item)) {

                    return true;
                }
            } catch (ClassNotFoundException ignored) {}
        }

        return false;
    }

    public static boolean isValidBow(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof net.minecraft.world.item.BowItem ||
            item instanceof net.minecraft.world.item.CrossbowItem) {
            return true;
        }

        for (ResourceLocation rl : ADDITIONAL_BOW_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        

        return false;
    }

    public static boolean isValidShield(ItemStack stack) {
        Item item = stack.getItem();

        if (item instanceof net.minecraft.world.item.ShieldItem) {
            return true;
        }

        for (ResourceLocation rl : ADDITIONAL_SHIELD_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidSpawner(Block block) {
        for (ResourceLocation rl : ADDITIONAL_SPAWNER_TAGS) {
            TagKey<Block> tag = BlockTags.create(rl);

            // Check if this block is in the tag
            if (block.defaultBlockState().is(tag)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isValidArmor(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_HELMET_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        for (ResourceLocation rl : ADDITIONAL_CHESTPLATE_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        for (ResourceLocation rl : ADDITIONAL_LEGGINGS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        for (ResourceLocation rl : ADDITIONAL_BOOTS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidHelmet(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_HELMET_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidChestplate(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_CHESTPLATE_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }
    public static boolean isValidLeggings(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_LEGGINGS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }
    public static boolean isValidBoots(ItemStack stack) {
        for (ResourceLocation rl : ADDITIONAL_BOOTS_TAGS) {
            if (stack.is(ItemTags.create(rl))) {
                return true;
            }
        }
        return false;
    }
}
