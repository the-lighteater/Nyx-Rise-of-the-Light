package dot.lighteater.nyx_rotl.item.custom;

import com.google.common.collect.Streams;
import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.capabilities.NyxWorld;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.stream.Stream;

public class MeteorFinder extends Item {

    public MeteorFinder(Properties properties) {
        super(properties);
    }

    @Mod.EventBusSubscriber(
            modid = "nyx_rotl",
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT
    )
    public static class ClientHandler {

        @SubscribeEvent
        public static void registerItemProperties(
                net.minecraftforge.client.event.RegisterClientReloadListenersEvent event
        ) {
            ItemProperties.register(
                    ModItems.METEOR_FINDER.get(),
                    new ResourceLocation("nyx_rotl", "angle"),
                    new MeteorFinderProperty()
            );
        }
    }

    private static class MeteorFinderProperty
            implements net.minecraft.client.renderer.item.ItemPropertyFunction {

        private double rotation;
        private double rota;
        private long lastUpdateTick;

        private BlockPos meteorPos;

        @Override
        public float call(
                ItemStack stack,
                @Nullable ClientLevel level,
                @Nullable LivingEntity entity,
                int seed
        ) {
            if (entity == null && !stack.isFramed()) {
                return 0.0F;
            }

            boolean hasEntity = entity != null;

            Entity source;

            if (hasEntity) {
                source = entity;
            } else {
                source = stack.getFrame();
            }

            if (source == null) {
                return 0.0F;
            }

            if (level == null && source.level() instanceof ClientLevel clientLevel) {
                level = clientLevel;
            }

            if (level == null) {
                return 0.0F;
            }

            /*
             * Original:
             *
             * double d1 = flag
             *      ? entity.rotationYaw
             *      : this.getFrameRotation((EntityItemFrame) entity);
             */

            double d1 = hasEntity
                    ? source.getYRot()
                    : getFrameRotation((ItemFrame) source);

            d1 = Mth.positiveModulo(
                    d1 / 360.0D,
                    1.0D
            );

            double d2 = getMeteorToAngle(level, source)
                    / (Math.PI * 2D);

            double d0;

            /*
             * Original behavior:
             * randomly spin if no meteor was found.
             */
            if (Double.isNaN(d2)) {
                d0 = Math.random();
            } else {
                d0 = 0.5D - (d1 - 0.25D - d2);
            }

            if (hasEntity) {
                d0 = wobble(level, d0);
            }

            return Mth.positiveModulo(
                    (float) d0,
                    1.0F
            );
        }

        private double wobble(
                ClientLevel level,
                double d
        ) {
            if (level.getGameTime() != lastUpdateTick) {
                lastUpdateTick = level.getGameTime();

                double d0 = d - rotation;

                d0 = Mth.positiveModulo(
                        d0 + 0.5D,
                        1.0D
                ) - 0.5D;

                rota += d0 * 0.1D;
                rota *= 0.8D;

                rotation = Mth.positiveModulo(
                        rotation + rota,
                        1.0D
                );
            }

            return rotation;
        }

        private double getFrameRotation(ItemFrame frame) {
            return Mth.wrapDegrees(
                    180 + frame.getDirection().get2DDataValue() * 90
            );
        }

        private double getMeteorToAngle(
                ClientLevel level,
                Entity entity
        ) {
            /*
             * Recalculate every 100 ticks, just like the old mod.
             */
            if (meteorPos == null
                    || level.getGameTime() % 100 == 0) {

                Stream<BlockPos> meteorLocations =
                        NyxWorld.clientMeteorLandingSites.stream();

//                if (Config.meteorCacheEnabled.get()) {
//                    meteorLocations = Streams.concat(
//                            meteorLocations,
//                            NyxWorld.clientCachedMeteorPositions.stream()
//                    );
//                }

                meteorPos = meteorLocations
                        .min(Comparator.comparingDouble(p -> entity.distanceToSqr(
                                p.getX() + 0.5D,
                                p.getY() + 0.5D,
                                p.getZ() + 0.5D
                        )))
                        .orElse(null);
            }

            if (meteorPos == null) {
                return Double.NaN;
            }

            return Math.atan2(
                    meteorPos.getZ() + 0.5D - entity.getZ(),
                    meteorPos.getX() + 0.5D - entity.getX()
            );
        }
    }
}