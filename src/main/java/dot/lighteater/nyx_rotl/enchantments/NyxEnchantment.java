package dot.lighteater.nyx_rotl.enchantments;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;

public class NyxEnchantment extends Enchantment {

    protected NyxEnchantment(
            Rarity rarity,
            EnchantmentCategory category,
            EquipmentSlot[] applicableSlots
    ) {
        super(rarity, category, applicableSlots);
    }
}