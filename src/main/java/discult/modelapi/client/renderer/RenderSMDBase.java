package discult.modelapi.client.renderer;

import discult.modelapi.client.LayerHeldItemTest;
import discult.modelapi.client.loaders.oldSMD.AnimationFrame;
import discult.modelapi.client.loaders.oldSMD.Bone;
import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.common.entities.EntitySMDBase;
import discult.modelapi.utils.Quaternion;
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
import org.lwjgl.opengl.GLSync;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.HashMap;

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




         float limbSwingAmount = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
         float limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
         if (limbSwingAmount > 1.0F) {
            limbSwingAmount = 1.0F;
         }



         this.getMainModel().setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
         this.getMainModel().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625F, entity);


         GlStateManager.scale(-1,-1,1);
         this.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 0.0625F);


        // GlStateManager.scale((float)Math.pow(-4,-1),(float)Math.pow(-4,-1),(float)Math.pow(-4,-1));

         renderSkeleton();



      } catch (Exception var17) {
         var17.printStackTrace();
      }


      GlStateManager.popMatrix();

   }


   private void renderSkeleton()
   {

      GlStateManager.scale(1,-1,-1);
      GlStateManager.rotate(90,1,0,0);
      GlStateManager.disableDepth();

      GL11. glDisable(GL11.GL_CULL_FACE);
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glDisable(GL11.GL_LIGHTING);


      for(Bone parent :  ((ModelSMDBase)getMainModel()).theModel.body.currentAnim.bones)
      {
         Vector4f screenPos = new Vector4f();
         screenPos.set(parent.getPosition());
         Matrix4f.transform(((ModelSMDBase)getMainModel()).theModel.body.currentAnim.frames.get(((ModelSMDBase)getMainModel()).theModel.body.currentAnim.currentFrameIndex).transforms.get(parent.ID), screenPos, screenPos);

         float x1 = screenPos.getX() /16, y1 = screenPos.getY() /16, z1 = screenPos.getZ() /16;


         for(Bone child : parent.children)
         {
            screenPos.set(child.getPosition());
            Matrix4f.transform(((ModelSMDBase)getMainModel()).theModel.body.currentAnim.frames.get(((ModelSMDBase)getMainModel()).theModel.body.currentAnim.currentFrameIndex).transforms.get(child.ID), screenPos, screenPos);
            float x2 = screenPos.getX() /16, y2 = screenPos.getY() /16, z2 = screenPos.getZ() /16;

            GL11.glColor4f(0.0f, 0.8f, 0.1f, 0.5f);
            drawLine(x1,y1,z1, x2,y2,z2);
            GL11.glColor3f(1f,1f,1f);

         }

         GL11.glColor4f(0.0f, 0.1f, 0.8f, 0.5f);
         drawDot(x1,y1,z1);
         GL11.glColor3f(1f,1f,1f);

      }

      GL11.glEnable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      GL11.glEnable(GL11.GL_ALPHA_TEST);

      GL11.glEnable(GL11.GL_LIGHTING);

      GlStateManager.enableDepth();
      GlStateManager.disableCull();

   }


   private void drawDot(float x, float y, float z)
   {
      GL11.glBegin(GL11.GL_POINTS);
      GL11.glPointSize(10F);
      GL11.glVertex3d(x,y,z);
      GL11.glEnd();
   }
   private void drawLine(float x, float y, float z, float cx, float cy, float cz)
   {
       GL11.glLineWidth(5F);
       GL11.glBegin(GL11.GL_LINE_STRIP);
       GL11.glVertex3d(x,y,z);
       GL11.glVertex3d(cx,cy,cz);
       GL11.glEnd();
   }

}
