package discult.modelapi.client.renderer;

import discult.modelapi.client.LayerHeldItemTest;
import discult.modelapi.client.loaders.oldSMD.AnimationFrame;
import discult.modelapi.client.loaders.oldSMD.Bone;
import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.common.entities.EntitySMDBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

@SideOnly(Side.CLIENT)
public abstract class RenderSMDBase<T extends EntityLiving> extends RenderLiving<T> {
   public RenderSMDBase(float shadowsizeIn) {
      super(Minecraft.getMinecraft().getRenderManager(), (ModelBase)null, shadowsizeIn);
   }

   LayerHeldItemTest test = new LayerHeldItemTest();

   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();

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
         GlStateManager.scale(-4,-4,-4);
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

      GlStateManager.scale(4,4,4);

      GlStateManager.popMatrix();

      GlStateManager.pushMatrix();

       GlStateManager.scale(0.5,0.5,0.5);



        this.renderLivingAt(entity,x,y,z);


        if(((ModelSMDBase)this.getMainModel()).theModel.body.currentFrame() != null)
        renderSkeleton();

      GlStateManager.popMatrix();

      /**
       * TEMP





      System.out.println("AnimationName: " + currentName);
      System.out.println("Current Frame: " + currentFrame + "\n" );
      System.out.println("Bone ID      : " + bone.ID);
      System.out.println("Bone Name    : " + bone.name);
      System.out.println("X: " + bone.getX(currentName, currentFrame));
      System.out.println("Y: " + bone.getY(currentName, currentFrame));
      System.out.println("Z: " + bone.getZ(currentName, currentFrame));
      System.out.println("XR: " + bone.bonePos.get(currentName).get(currentFrame).xr);
      System.out.println("YR: " + bone.bonePos.get(currentName).get(currentFrame).yr);
      System.out.println("ZR: " + bone.bonePos.get(currentName).get(currentFrame).zr + "\n");


       test.doRender(entity, (float)x, (float)y, (float)z);
       */

   }

   private void renderSkeleton()
   {
      AnimationFrame currentFrame = ((ModelSMDBase)this.getMainModel()).theModel.body.currentAnim.frames.get(((ModelSMDBase)this.getMainModel()).theModel.body.currentAnim.currentFrameIndex);
      GL11.glBegin(GL11.GL_LINES);

      ((ModelSMDBase)this.getMainModel()).theModel.allBones.forEach(a -> {


         Vector3f pos = new Vector3f(currentFrame.transforms.get(a.ID).m03, currentFrame.transforms.get(a.ID).m13, currentFrame.transforms.get(a.ID).m23);

         // if(a.getPosition() != null)
        // GL11.glVertex3f(a.getPosition().x,a.getPosition().y,a.getPosition().z);

         ((ModelSMDBase)this.getMainModel()).theModel.allBones.forEach(b -> {

            if(b.ID == a.parent.ID)
            {
               System.out.println("Got into the bone parent ID");
               Vector3f newPos =new Vector3f(currentFrame.transforms.get(b.ID).m03, currentFrame.transforms.get(b.ID).m13, currentFrame.transforms.get(b.ID).m23);

               //    if(a.getPosition() != null)
           //    GL11.glVertex3f(b.getPosition().x,b.getPosition().y,b.getPosition().z);
            }

         });

      });


      GL11.glEnd();
/*
      GL11.glBegin(GL11.GL_POINTS);
      ((ModelSMDBase)this.getMainModel()).theModel.allBones.forEach(a -> {

         GL11.glVertex3f(a.getPosition().x,a.getPosition().y,a.getPosition().z);
      });

      GL11.glEnd();

       */
   }
}
