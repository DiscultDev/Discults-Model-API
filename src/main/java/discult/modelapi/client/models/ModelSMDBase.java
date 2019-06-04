package discult.modelapi.client.models;

import discult.modelapi.client.loaders.oldSMD.SMDAnimation;
import discult.modelapi.client.loaders.oldSMD.ValveStudioModel;
import discult.modelapi.common.entities.EntitySMDBase;
import discult.modelapi.common.entities.animation.IncrementingVariable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelSMDBase extends ModelBase {
   public EntitySMDBase mob;
   protected float animationIncrement = 1.0F;
   public ValveStudioModel theModel;
   protected float movementThreshold = 0.2F;
   public String path;
   public String name;

   public ModelSMDBase(String path, String name) {
      this.path = path;
      this.name = name;
   }

   public boolean entityMoving(Entity entity) {
      this.mob = (EntitySMDBase)entity;
      return this.mob.limbSwingAmount > this.movementThreshold;
   }

   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
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
}
