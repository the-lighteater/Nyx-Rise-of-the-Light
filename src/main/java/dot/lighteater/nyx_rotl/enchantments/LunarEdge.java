package dot.lighteater.nyx_rotl.enchantments;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class LunarEdge extends NyxEnchantment {

    public LunarEdge() {
        super(
                Rarity.UNCOMMON,
                EnchantmentCategory.WEAPON,
                new EquipmentSlot[]{
                        EquipmentSlot.MAINHAND,
                        EquipmentSlot.OFFHAND
                }
        );
    }

    @Override
    public float getDamageBonus(
            int level,
            MobType mobType,
            ItemStack enchantedItem
    ) {
        float damagePerLevel =
                (float) (
                        Config.maxLevelLunarEdgeDamage.get()
                                - Config.minLevelLunarEdgeDamage.get()
                )
                        / (getMaxLevel() - 1);

        Double additionalDamage =
                (Config.minLevelLunarEdgeDamage.get()
                                                        + Math.max(0, level - 1) * damagePerLevel);

        // Prevent floating-point values such as 3.00001
        // at the maximum level.
        if (level == getMaxLevel()) {
            additionalDamage =
                    Config.maxLevelLunarEdgeDamage.get();
        }

        float finalDamage = (float) (
                Config.baseLunarEdgeDamage.get()
                        + NyxWorld.moonPhase * additionalDamage
        );

        NyxROTL.LOGGER.debug(String.valueOf(finalDamage));

        return finalDamage;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other)
                && other != Enchantments.SHARPNESS
                && other != Enchantments.SMITE
                && other != Enchantments.BANE_OF_ARTHROPODS;
    }


    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof AxeItem
                || super.canEnchant(stack);
    }
}