package discult.modelapi.client;

import discult.modelapi.client.loaders.oldSMD.Bone;
import discult.modelapi.client.loaders.oldSMD.BonePos;
import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.common.entities.EntitySMDBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class LayerHeldItemTest implements LayerRenderer<EntityLivingBase>
{

    float tx, ty,tz;
    public void doRender(EntityLivingBase entity, float x, float y, float z)
    {
        if(entity instanceof EntitySMDBase)
        {
            ModelSMDBase model = ((EntitySMDBase) entity).getModel();

            GlStateManager.pushMatrix();

            /*
            Bone bone = model.theModel.currentAnimation.bones.get(1);
            BonePos pos = bone.bonePos.get(model.theModel.currentAnimation.animationName).get(model.theModel.currentAnimation.currentFrameIndex);
            GlStateManager.scale(1,1,1);
            GlStateManager.translate(x + bone.getX(model.theModel.currentAnimation.animationName, model.theModel.currentAnimation.currentFrameIndex), y + bone.getY(model.theModel.currentAnimation.animationName, model.theModel.currentAnimation.currentFrameIndex),z + bone .getZ(model.theModel.currentAnimation.animationName, model.theModel.currentAnimation.currentFrameIndex));
            GlStateManager.rotate(pos.xr,1,0,0);
            GlStateManager.rotate(pos.yr,0,1,0);
            GlStateManager.rotate(pos.zr,0,0,1);


             */
            tx = model.theModel.currentAnimation.bones.get(2).modified.m03;
            ty = model.theModel.currentAnimation.bones.get(2).modified.m13;
            tz = model.theModel.currentAnimation.bones.get(2).modified.m23;


            Minecraft.getMinecraft().getItemRenderer().renderItem(entity, new ItemStack(Items.IRON_SWORD), ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }


    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {


    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
