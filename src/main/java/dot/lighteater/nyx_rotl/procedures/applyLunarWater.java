package dot.lighteater.nyx_rotl.procedures;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class applyLunarWater {

    public static boolean execute(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return false;
        }

        if (livingEntity.level().isClientSide()) {
            return false;
        }

        boolean did = false;

        // Remove all negative potion effects.
        List<MobEffect> effectsToRemove = new ArrayList<>();

        for (MobEffectInstance effect : livingEntity.getActiveEffects()) {
            MobEffect mobEffect = effect.getEffect();

            if (mobEffect.isBeneficial()) {
                continue;
            }

            effectsToRemove.add(mobEffect);
        }

        for (MobEffect effect : effectsToRemove) {
            livingEntity.removeEffect(effect);
            did = true;
        }

        // Add Regeneration II for 5 seconds if not already active.
        if (!livingEntity.hasEffect(MobEffects.REGENERATION)) {
            livingEntity.addEffect(
                    new MobEffectInstance(
                            MobEffects.REGENERATION,
                            5 * 20,
                            1
                    )
            );
            did = true;
        }

        return did;
    }
}