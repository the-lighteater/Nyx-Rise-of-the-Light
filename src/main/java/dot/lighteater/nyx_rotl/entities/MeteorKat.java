package dot.lighteater.nyx_rotl.entities;


import dot.lighteater.nyx_rotl.item.ModItems;
import dot.lighteater.nyx_rotl.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;

public class MeteorKat extends Cat {

    public MeteorKat(EntityType<? extends Cat> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.animal.Cat.createAttributes().add(Attributes.MAX_HEALTH, 16.0D);}

    @Override
    public boolean fireImmune() {
        return true;}

    @Override
    public Cat getBreedOffspring(ServerLevel level, AgeableMob partner) {
        Cat child;

        if (this.random.nextBoolean()) {
            child = EntityType.CAT.create(level);
        } else {
            child = ModEntities.METEOR_KAT.get().create(level);
        }

        if (child != null && this.isTame()) {
            child.setTame(true);

            if (this.getOwnerUUID() != null) {
                child.setOwnerUUID(this.getOwnerUUID());
            }

            child.setCollarColor(this.getCollarColor());
        }

        return child;
    }

    @Override
    protected void dropCustomDeathLoot(
            net.minecraft.world.damagesource.DamageSource source,
            int looting,
            boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);

        this.spawnAtLocation(ModItems.METEOR_SHARD.get());
    }
}