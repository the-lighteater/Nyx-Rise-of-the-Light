package dot.lighteater.nyx_rotl.entities;

import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.lunarevents.BloodMoon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;

public class WolfAISpecialMoon extends NearestAttackableTargetGoal<LivingEntity> {

    private final Wolf wolf;

    public WolfAISpecialMoon(Wolf wolf) {
        super(wolf, LivingEntity.class, 10, true, false, WolfAISpecialMoon::isValidTarget);
        this.wolf = wolf;
    }

    // -----------------------------
    // TARGET FILTER
    // -----------------------------
    private static boolean isValidTarget(LivingEntity e) {

        if (e instanceof Wolf) return false;

        return (e instanceof Player)
                || (e instanceof net.minecraft.world.entity.animal.Animal)
                || (e instanceof Skeleton);
    }

    // -----------------------------
    // ONLY ACTIVE DURING EVENTS
    // -----------------------------
    @Override
    public boolean canUse() {
        return super.canUse() && shouldHappen();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && shouldHappen();
    }

    private boolean shouldHappen() {

        NyxWorld nyx = NyxWorld.get((ServerLevel) wolf.level());

        if (nyx.currentEvent == null)
            return false;

        return (nyx.currentEvent.equals("full_moon"))
                || (nyx.currentEvent.equals("blood_moon"));
    }
}