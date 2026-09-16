package dot.lighteater.nyx_rotl.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.event.ModClientEvents;
import dot.lighteater.nyx_rotl.lunarevents.CelestialEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    // ============================================================
    // SUN
    // ============================================================

    @ModifyArg(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
                    ordinal = 0
            ),
            index = 1
    )
    private ResourceLocation nyx$modifySunTexture(
            ResourceLocation original
    ) {

        CelestialEvent event = ModClientEvents.getCurrentClientEvent();

        if (event == null) {
            return original;
        }

        String sunTexture = event.getSunTexture();

        if (sunTexture == null) {
            return original;
        }

        return new ResourceLocation(
                NyxROTL.MODID,
                "textures/sun/" + sunTexture + ".png"
        );
    }

    // ============================================================
    // MOON
    // ============================================================

    @ModifyArg(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
                    ordinal = 1
            ),
            index = 1
    )
    private ResourceLocation nyx$modifyMoonTexture(
            ResourceLocation original
    ) {

        CelestialEvent event = ModClientEvents.getCurrentClientEvent();

        if (event == null) {
            return original;
        }

        String moonTexture = event.getMoonTexture();

        if (moonTexture == null) {
            return original;
        }

        return new ResourceLocation(
                NyxROTL.MODID,
                "textures/moon/" + moonTexture + ".png"
        );
    }

    // ============================================================
    // MOON SIZE
    // ============================================================
    @ModifyConstant(
            method = "renderSky",
            constant = @org.spongepowered.asm.mixin.injection.Constant(
                    floatValue = 20.0F
            )
    )
    private float nyx$modifyMoonSize(float original) {

        CelestialEvent event = ModClientEvents.getCurrentClientEvent();

        if (event == null) {
            return original;
        }

        return original * event.getMoonSizeMultiplier();
    }

    // ============================================================
// SUN SIZE
// ============================================================
    @ModifyConstant(
            method = "renderSky",
            constant = @org.spongepowered.asm.mixin.injection.Constant(
                    floatValue = 30.0F
            )
    )
    private float nyx$modifySunSize(float original) {

        CelestialEvent event = ModClientEvents.getCurrentClientEvent();

        if (event == null) {
            return original;
        }

        return original * event.getSunSizeMultiplier();
    }

    // ============================================================
// ECLIPSE MOON MOVEMENT
// ============================================================

    @Inject(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
                    ordinal = 1
            )
    )
    private void nyx$moveEclipseMoon(
            PoseStack poseStack,
            Matrix4f projectionMatrix,
            float partialTick,
            Camera camera,
            boolean isFoggy,
            Runnable skyFogSetup,
            CallbackInfo ci
    ) {

        CelestialEvent event = ModClientEvents.getCurrentClientEvent();

        if (event == null) {
            return;
        }

        if (!event.isSolarEvent()) {
            return;
        }

        float angle = event.getMoonEclipseAngle();

        if (angle == 0.0F) {
            return;
        }

        poseStack.mulPose(
                Axis.ZP.rotationDegrees(angle)
        );
    }
}