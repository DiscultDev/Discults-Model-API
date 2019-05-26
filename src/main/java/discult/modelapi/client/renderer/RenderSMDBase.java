package discult.modelapi.client.renderer;

import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.common.entities.EntitySMDBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderSMDBase<T extends EntityLiving> extends RenderLiving<T> {
   public RenderSMDBase(float shadowsizeIn) {
      super(Minecraft.getMinecraft().getRenderManager(), (ModelBase)null, shadowsizeIn);
   }

   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();

      //this.mainModel = ((EntitySMDBase)entity.getM);
      this.mainModel = ((EntitySMDBase)entity).getModel();
      this.getMainModel().swingProgress = this.getSwingProgress(entity, partialTicks);

      try {
         float f2 = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
         float f3 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
         float netHeadYaw = f3 - f2;
         float headPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
         this.renderLivingAt(entity, x, y, z);
         float ageInTicks = this.handleRotationFloat(entity, partialTicks);
         this.applyRotations(entity, ageInTicks, f2, partialTicks);
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableLighting();
         GlStateManager.scale(-1,-1,1);
         float limbSwingAmount = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
         float limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
         if (limbSwingAmount > 1.0F) {
            limbSwingAmount = 1.0F;
         }

         GlStateManager.enableAlpha();
         this.getMainModel().setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
         this.getMainModel().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625F, entity);
         GlStateManager.enableCull();
         this.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625F);
         GlStateManager.disableCull();
      } catch (Exception var17) {
         var17.printStackTrace();
      }

      GlStateManager.popMatrix();
   }
}
