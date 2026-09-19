package dot.lighteater.nyx_rotl.entities;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ModelMeteorKat extends CatModel<MeteorKat> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(NyxROTL.MODID, "meteor_kat"),
                    "main"
            );

    public ModelMeteorKat(ModelPart root) {
        super(root);
    }

    /**

     Creates the vanilla Cat model and adds the MeteorKat helmet
     as a child of the cat's head.*/
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = CatModel.createBodyMesh(CubeDeformation.NONE);

        PartDefinition root = mesh.getRoot();

        PartDefinition head = root.getChild("head");

        head.addOrReplaceChild(
                "helmet",
                CubeListBuilder.create()
                        .texOffs(38, 19)
                        .addBox(
                                -3.0F,
                                -3.5F,
                                -4.5F,
                                6.0F,
                                6.0F,
                                7.0F
                        ),
                PartPose.ZERO
        );

        return LayerDefinition.create(mesh, 64, 32);
    }
}