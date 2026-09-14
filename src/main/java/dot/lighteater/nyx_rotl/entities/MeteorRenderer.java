package dot.lighteater.nyx_rotl.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MeteorRenderer extends EntityRenderer<FallingMeteor> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    "nyx_rotl",
                    "textures/entity/meteor.png"
            );

    public MeteorRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            FallingMeteor entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        if (!entity.isLoaded()) {
            return;
        }

        poseStack.pushPose();

        float size = entity.getSize() / 2.0F;

        poseStack.scale(size, size, size);

        VertexConsumer consumer =
                buffer.getBuffer(RenderType.entitySolid(TEXTURE));

        renderCube(
                poseStack,
                consumer,
                packedLight
        );

        poseStack.popPose();

        super.render(
                entity,
                entityYaw,
                partialTicks,
                poseStack,
                buffer,
                packedLight
        );
    }

    private void renderCube(
            PoseStack poseStack,
            VertexConsumer buffer,
            int light
    ) {
        var pose = poseStack.last();

        float x0 = 0.0F;
        float y0 = 0.0F;
        float z0 = 0.0F;

        float x1 = 1.0F;
        float y1 = 1.0F;
        float z1 = 1.0F;

        // -----------------------------------
        // FRONT (+Z)
        // -----------------------------------

        quad(
                pose,
                buffer,
                light,

                x0, y0, z1,
                x1, y0, z1,
                x1, y1, z1,
                x0, y1, z1,

                0.0F, 0.25F,
                0.25F, 0.25F,
                0.25F, 0.5F,
                0.0F, 0.5F,

                0, 0, 1
        );

        // -----------------------------------
        // BACK (-Z)
        // -----------------------------------

        quad(
                pose,
                buffer,
                light,

                x1, y0, z0,
                x0, y0, z0,
                x0, y1, z0,
                x1, y1, z0,

                0.0F, 0.25F,
                0.25F, 0.25F,
                0.25F, 0.5F,
                0.0F, 0.5F,

                0, 0, -1
        );

        // -----------------------------------
        // LEFT (-X)
        // -----------------------------------

        quad(
                pose,
                buffer,
                light,

                x0, y0, z0,
                x0, y0, z1,
                x0, y1, z1,
                x0, y1, z0,

                0.0F, 0.25F,
                0.25F, 0.25F,
                0.25F, 0.5F,
                0.0F, 0.5F,

                -1, 0, 0
        );

        // -----------------------------------
        // RIGHT (+X)
        // -----------------------------------

        quad(
                pose,
                buffer,
                light,

                x1, y0, z1,
                x1, y0, z0,
                x1, y1, z0,
                x1, y1, z1,

                0.0F, 0.25F,
                0.25F, 0.25F,
                0.25F, 0.5F,
                0.0F, 0.5F,

                1, 0, 0
        );

        // -----------------------------------
        // TOP (+Y)
        // -----------------------------------

        quad(
                pose,
                buffer,
                light,

                x0, y1, z1,
                x1, y1, z1,
                x1, y1, z0,
                x0, y1, z0,

                0.0F, 0.25F,
                0.25F, 0.25F,
                0.25F, 0.5F,
                0.0F, 0.5F,

                0, 1, 0
        );

        // -----------------------------------
        // BOTTOM (-Y)
        // -----------------------------------

        quad(
                pose,
                buffer,
                light,

                x0, y0, z0,
                x1, y0, z0,
                x1, y0, z1,
                x0, y0, z1,

                0.0F, 0.25F,
                0.25F, 0.25F,
                0.25F, 0.5F,
                0.0F, 0.5F,

                0, -1, 0
        );
    }

    private void quad(
            com.mojang.blaze3d.vertex.PoseStack.Pose pose,
            VertexConsumer buffer,
            int light,

            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,

            float u0, float v0,
            float u1, float v1,
            float u2, float v2,
            float u3, float v3,

            float nx, float ny, float nz
    ) {
        vertex(
                pose,
                buffer,
                light,
                x0, y0, z0,
                u0, v0,
                nx, ny, nz
        );

        vertex(
                pose,
                buffer,
                light,
                x1, y1, z1,
                u1, v1,
                nx, ny, nz
        );

        vertex(
                pose,
                buffer,
                light,
                x2, y2, z2,
                u2, v2,
                nx, ny, nz
        );

        vertex(
                pose,
                buffer,
                light,
                x3, y3, z3,
                u3, v3,
                nx, ny, nz
        );
    }

    private void vertex(
            com.mojang.blaze3d.vertex.PoseStack.Pose pose,
            VertexConsumer buffer,
            int light,
            float x,
            float y,
            float z,
            float u,
            float v,
            float nx,
            float ny,
            float nz
    ) {
        buffer.vertex(
                        pose.pose(),
                        x, y, z
                )
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(0)
                .uv2(light)
                .normal(
                        pose.normal(),
                        nx, ny, nz
                )
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(
            FallingMeteor entity
    ) {
        return TEXTURE;
    }
}