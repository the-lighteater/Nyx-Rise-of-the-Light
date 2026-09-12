package dot.lighteater.nyx_rotl.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class MeteorRenderer extends EntityRenderer<FallingMeteor> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("nyx_rotl", "textures/entity/meteor.png");

    public MeteorRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(FallingMeteor entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        if (!entity.isLoaded()) return;

        poseStack.pushPose();

        Vec3 pos = entity.position();
        poseStack.translate(0, 0, 0);

        float size = entity.getSize() / 2f;
        poseStack.scale(size, size, size);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entitySolid(TEXTURE));

        renderCube(poseStack, consumer, packedLight);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderCube(PoseStack poseStack, VertexConsumer buffer, int light) {
        var pose = poseStack.last();

        // simple 32x32x32 cube like your ModelRenderer box
        float s = 1.0f;

        // front face (example; full cube would include 6 faces)
        buffer.vertex(pose.pose(), 0, 0, 0)
                .color(255, 255, 255, 255)
                .uv(0, 0)
                .overlayCoords(0)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();

        buffer.vertex(pose.pose(), s, 0, 0)
                .color(255, 255, 255, 255)
                .uv(1, 0)
                .overlayCoords(0)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();

        buffer.vertex(pose.pose(), s, s, 0)
                .color(255, 255, 255, 255)
                .uv(1, 1)
                .overlayCoords(0)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();

        buffer.vertex(pose.pose(), 0, s, 0)
                .color(255, 255, 255, 255)
                .uv(0, 1)
                .overlayCoords(0)
                .uv2(light)
                .normal(0, 0, 1)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(FallingMeteor entity) {
        return TEXTURE;
    }
}