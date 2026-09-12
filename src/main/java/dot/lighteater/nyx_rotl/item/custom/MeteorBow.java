package dot.lighteater.nyx_rotl.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public class MeteorBow extends BowItem {

    public MeteorBow(Properties properties) {
        super(properties.durability(2250));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        boolean infinite = player.getAbilities().instabuild ||
                EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;

        ItemStack ammo = player.getProjectile(stack);

        int charge = this.getUseDuration(stack) - timeLeft;

        charge = ForgeEventFactory.onArrowLoose(stack, level, player, charge, !ammo.isEmpty() || infinite);
        if (charge < 0) return;

        if (!ammo.isEmpty() || infinite) {
            if (ammo.isEmpty()) {
                ammo = new ItemStack(Items.ARROW);
            }

            float velocity = getVelocity(charge);

            if (velocity >= 0.1F) {
                boolean infiniteAmmo = player.getAbilities().instabuild ||
                        (ammo.getItem() instanceof ArrowItem arrowItem &&
                                arrowItem.isInfinite(ammo, stack, player));

                if (!level.isClientSide) {
                    ArrowItem arrowItem = (ArrowItem)(ammo.getItem() instanceof ArrowItem ? ammo.getItem() : Items.ARROW);
                    AbstractArrow arrow = arrowItem.createArrow(level, ammo, player);

                    arrow = customArrow(arrow);

                    // your custom velocity scaling
                    arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,
                            velocity * 3.0F * 1.3F, 1.0F);

                    if (velocity == 1.0F) {
                        arrow.setCritArrow(true);
                    }

                    int power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                    if (power > 0) {
                        arrow.setBaseDamage(arrow.getBaseDamage() + power * 0.5D + 0.5D);
                    }

                    int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
                    if (punch > 0) {
                        arrow.setKnockback(punch);
                    }

                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                        arrow.setSecondsOnFire(5);
                    }

                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));

                    if (infiniteAmmo) {
                        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    // your custom damage multiplier
                    arrow.setBaseDamage(arrow.getBaseDamage() *
                            // TODO: Config.bowDamageMultiplier
                            1.75
                    );

                    level.addFreshEntity(arrow);
                }

                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                        1.0F,
                        1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                if (!infiniteAmmo && !player.getAbilities().instabuild) {
                    ammo.shrink(1);
                    if (ammo.isEmpty()) {
                        player.getInventory().removeItem(ammo);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    private static float getVelocity(int charge) {
        float f = (float) charge / 30.0F; // your 30 tick change
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getEnchantmentValue() {
        return 18;
    }
}