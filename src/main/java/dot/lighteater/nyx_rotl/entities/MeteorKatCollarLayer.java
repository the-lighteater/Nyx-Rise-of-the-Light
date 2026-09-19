package dot.lighteater.nyx_rotl.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class MeteorKatCollarLayer
        extends RenderLayer<MeteorKat, ModelMeteorKat> {

    private static final ResourceLocation CAT_COLLAR_LOCATION =
            new ResourceLocation(
                    "textures/entity/cat/cat_collar.png"
            );

    private final ModelMeteorKat collarModel;

    public MeteorKatCollarLayer(
            RenderLayerParent<MeteorKat, ModelMeteorKat> renderer,
            EntityModelSet modelSet
    ) {
        super(renderer);

        this.collarModel = new ModelMeteorKat(
                modelSet.bakeLayer(ModelLayers.CAT_COLLAR)
        );
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            MeteorKat entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (entity.isTame()) {
            float[] colors =
                    entity.getCollarColor().getTextureDiffuseColors();

            coloredCutoutModelCopyLayerRender(
                    this.getParentModel(),
                    this.collarModel,
                    CAT_COLLAR_LOCATION,
                    poseStack,
                    buffer,
                    packedLight,
                    entity,
                    limbSwing,
                    limbSwingAmount,
                    ageInTicks,
                    netHeadYaw,
                    headPitch,
                    partialTicks,
                    colors[0],
                    colors[1],
                    colors[2]
            );
        }
    }
}