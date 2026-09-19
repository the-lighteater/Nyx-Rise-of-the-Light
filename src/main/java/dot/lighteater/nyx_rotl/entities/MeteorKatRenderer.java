package dot.lighteater.nyx_rotl.entities;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class MeteorKatRenderer
        extends MobRenderer<MeteorKat, ModelMeteorKat> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    NyxROTL.MODID,
                    "textures/entity/meteor_kat.png"
            );

    public MeteorKatRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ModelMeteorKat(
                        context.bakeLayer(ModelMeteorKat.LAYER_LOCATION)
                ),
                0.4F
        );

        this.addLayer(
                new MeteorKatCollarLayer(
                        this,
                        context.getModelSet()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorKat entity) {
        return TEXTURE;
    }

    /**
     * Client-side model/renderer registration.
     */
    @Mod.EventBusSubscriber(
            modid = NyxROTL.MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT
    )
    public static class ClientEvents {

        @SubscribeEvent
        public static void registerLayerDefinitions(
                EntityRenderersEvent.RegisterLayerDefinitions event
        ) {
            event.registerLayerDefinition(
                    ModelMeteorKat.LAYER_LOCATION,
                    ModelMeteorKat::createBodyLayer
            );
        }

        @SubscribeEvent
        public static void registerRenderers(
                EntityRenderersEvent.RegisterRenderers event
        ) {
            event.registerEntityRenderer(
                    ModEntities.METEOR_KAT.get(),
                    MeteorKatRenderer::new
            );
        }
    }
}