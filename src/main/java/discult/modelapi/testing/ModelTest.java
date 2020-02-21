package discult.modelapi.testing;

import discult.modelapi.client.loaders.oldSMD.ValveStudioModel;
import discult.modelapi.client.models.ModelCustomWrapper;
import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.client.models.SMDModelRenderer;
import discult.modelapi.main.RenderLocation;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelTest extends ModelSMDBase
{
   public ModelTest()
   {
      super("test", "test");
      this.theModel = RenderLocation.setMobModel(path, name);
      this.setModelScaleXYZ(0.05f);
   }
   /*
   SMDModelRenderer body = new SMDModelRenderer(this, "body");

   public ModelTest() {
      super("test", "test");
      ValveStudioModel model = RenderLocation.setMobModel(this.path, this.name);
      this.body.addCustomModel(new ModelCustomWrapper(model));
      this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.body.rotateAngleX = -1.5707964F;
      this.theModel = model;
      this.setAnimationIncrement(1.0F);
   }

   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
      this.body.render(scale);
   }

   @SideOnly(Side.CLIENT)
   public void doAnimation(Entity entity) {
      super.doAnimation(entity);
      if (this.entityMoving(this.mob)) {
         if (this.mob.animationSwap) {
            this.setAnimation("walk");
         }
      } else if (this.mob.animationSwap) {
         this.setAnimation("idle");
      }

   }
    */
}
