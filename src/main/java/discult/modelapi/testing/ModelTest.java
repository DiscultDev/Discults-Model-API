package discult.modelapi.testing;

import discult.modelapi.client.loaders.oldSMD.Socket;
import discult.modelapi.client.loaders.oldSMD.ValveStudioModel;
import discult.modelapi.client.models.ModelCustomWrapper;
import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.client.models.SMDModelRenderer;
import discult.modelapi.main.RenderLocation;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GLSync;
import org.lwjgl.util.vector.Vector4f;

public class ModelTest extends ModelSMDBase {
   SMDModelRenderer body = new SMDModelRenderer(this, "body");
   public ModelRenderer head = new ModelRenderer((this), 0,0);

   private Vector4f pos = new Vector4f(0,0,0,1);


   public ModelTest() {
      super("test", "test");
      ValveStudioModel model = RenderLocation.setMobModel(this.path, this.name);
      this.body.addCustomModel(new ModelCustomWrapper(model));
      this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.body.rotateAngleX = -1.5707964F;
      this.theModel = model;
      this.setAnimationIncrement(1.0F);
      this.sockets.add(new Socket("testSocket",18, this));

      head.addBox(-4,-4,-4, 8,8,8, 1);
      head.setRotationPoint(0,0,0);
   }

   public void update(Vector4f pos)
   {
      this.pos = pos;
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
}
