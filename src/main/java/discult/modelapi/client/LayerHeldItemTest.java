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
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class LayerHeldItemTest implements LayerRenderer<EntityLivingBase>
{

    public void doRender(EntityLivingBase entity, double x, double y, double z)
    {
        if(entity instanceof EntitySMDBase)
        {
            ModelSMDBase model = ((EntitySMDBase) entity).getModel();

            GlStateManager.pushMatrix();

            Vector4f screenPos = new Vector4f(0,0,0,1);
            Matrix4f.transform(model.theModel.body.currentAnim.frames.get(model.theModel.body.currentAnim.currentFrameIndex).transforms.get(18), screenPos, screenPos);
            System.out.println(model.theModel.body.currentAnim.bones.get(18).name);

            float x1 = screenPos.getX()  /16, y1 = screenPos.getY() / 16 , z1 = screenPos.getZ() /16;

            GlStateManager.translate(x1, y1 ,z1);
            GlStateManager.scale(0.5,-0.5,-0.5);
            GlStateManager.rotate(45,-1,0,0);
            GlStateManager.rotate(90,0,-1,0);
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
