package discult.modelapi.client.models;

import discult.modelapi.client.loaders.oldSMD.Bone;
import discult.modelapi.client.loaders.oldSMD.SMDAnimation;
import discult.modelapi.client.loaders.oldSMD.ValveStudioModel;
import discult.modelapi.common.entities.EntitySMDBase;
import discult.modelapi.common.entities.animation.IncrementingVariable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class ModelSMDBase extends ModelBase {
   public EntitySMDBase mob;
   protected float animationIncrement = 1.0F;
   public ValveStudioModel theModel;
   protected float movementThreshold = 0.2F;
   public String path;
   public String name;

   public static final float initRotFix = -90.0F;
   public static final float offsetFixY = -0.25f;
   public static final float offsetFixZ = 1.5f;
   private float modelScaleX = 1, modelScaleY = 1, modelScaleZ = 1;

   public ModelSMDBase(String path, String name) {
      this.path = path;
      this.name = name;
   }

   public void setModelScaleX(float modelScaleX) {
      this.modelScaleX = modelScaleX;
   }

   public void setModelScaleY(float modelScaleY) {
      this.modelScaleY = modelScaleY;
   }

   public void setModelScaleZ(float modelScaleZ) {
      this.modelScaleZ = modelScaleZ;
   }

   public void setModelScaleXYZ(float scale)
   {
      this.modelScaleX = scale;
      this.modelScaleY = scale;
      this.modelScaleZ = scale;
   }

   public boolean entityMoving(Entity entity) {
      this.mob = (EntitySMDBase)entity;
      return this.mob.limbSwingAmount > this.movementThreshold;
   }

   @Override
   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

      setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

      GlStateManager.pushMatrix();

      GlStateManager.rotate(initRotFix, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(0.0F, offsetFixY, offsetFixZ);
      GlStateManager.scale(modelScaleX, modelScaleY, modelScaleZ);

      theModel.renderAll();
      renderSkeleton();

      GlStateManager.popMatrix();
   }

   protected void setAnimationIncrement(float increment) {
      this.animationIncrement = increment;
   }

   public void doAnimation(Entity entity) {
      this.mob = (EntitySMDBase)entity;
      IncrementingVariable inc = this.getCounter(-1, this.mob);
      if (inc == null) {
         this.setCounter(-1, 2.14748365E9F, this.animationIncrement, this.mob);
      } else {
         inc.increment = this.animationIncrement;
      }

      if (!this.mob.animationCounting) {
         this.setAnimation("idle");
         this.mob.animationCounting = true;
      }

      if (this.theModel.hasAnimations()) {
         this.tickAnimation(this.mob);
      }

   }

   private void tickAnimation(EntitySMDBase mob) {
      SMDAnimation theAnim = this.theModel.currentAnimation;
      int frame = (int)Math.floor((double)(this.getCounter(-1, mob).value % (float)theAnim.totalFrames));
      theAnim.setCurrentFrame(frame);
      this.theModel.animate();
   }

   protected void setAnimation(String string) {
      this.theModel.setAnimation(string);
   }

   public static boolean isMinecraftPaused() {
      Minecraft m = Minecraft.getMinecraft();
      return m.isSingleplayer() && m.currentScreen != null && m.currentScreen.doesGuiPauseGame() && !m.getIntegratedServer().getPublic();
   }

   protected void setInt(int id, int value, EntitySMDBase mob) {
      mob.getAnimationVariables().setInt(id, value);
   }

   protected int getInt(int id, EntitySMDBase mob) {
      return mob.getAnimationVariables().getInt(id);
   }

   private IncrementingVariable setCounter(int id, float limit, float increment, EntitySMDBase mob) {
      mob.getAnimationVariables().setCounter(id, limit, increment);
      return this.getCounter(id, mob);
   }

   private IncrementingVariable getCounter(int id, EntitySMDBase mob) {
      return mob.getAnimationVariables().getCounter(id);
   }

   private void renderSkeleton()
   {

      GlStateManager.scale(1,-1,-1);
      //GlStateManager.rotate(90,1,0,0);
      GlStateManager.disableDepth();

      GL11. glDisable(GL11.GL_CULL_FACE);
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glDisable(GL11.GL_LIGHTING);


      for(Bone parent :  theModel.body.currentAnim.bones)
      {
         Vector4f screenPos = new Vector4f();
         screenPos.set(parent.getPosition());
         Matrix4f.transform(theModel.body.currentAnim.frames.get(theModel.body.currentAnim.currentFrameIndex).transforms.get(parent.ID), screenPos, screenPos);

         float x1 = screenPos.getX(), y1 = screenPos.getY(), z1 = screenPos.getZ();


         for(Bone child : parent.children)
         {
            screenPos.set(child.getPosition());
            Matrix4f.transform(theModel.body.currentAnim.frames.get(theModel.body.currentAnim.currentFrameIndex).transforms.get(child.ID), screenPos, screenPos);
            float x2 = screenPos.getX(), y2 = screenPos.getY(), z2 = screenPos.getZ();

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
