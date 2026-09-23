package dot.lighteater.nyx_rotl.enchantments;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class LunarShield extends NyxEnchantment {

    public LunarShield() {
        super(
                Rarity.UNCOMMON,
                EnchantmentCategory.ARMOR,
                new EquipmentSlot[]{
                        EquipmentSlot.HEAD,
                        EquipmentSlot.CHEST,
                        EquipmentSlot.LEGS,
                        EquipmentSlot.FEET
                }
        );
    }

    @Override
    public int getDamageProtection(
            int level,
            DamageSource source
    ) {
        if (source.isCreativePlayer()) {
            return 0;
        }

        int finalProtection = (int) Math.floor(
                (level + 1) * NyxWorld.moonPhase
        );

        NyxROTL.LOGGER.debug(String.valueOf(finalProtection));

        return finalProtection;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return super.checkCompatibility(other)
                && other != Enchantments.ALL_DAMAGE_PROTECTION
                && other != Enchantments.FIRE_PROTECTION
                && other != Enchantments.FALL_PROTECTION
                && other != Enchantments.BLAST_PROTECTION
                && other != Enchantments.PROJECTILE_PROTECTION;
    }
}