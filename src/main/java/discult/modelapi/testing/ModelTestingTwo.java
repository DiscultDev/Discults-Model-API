package discult.modelapi.testing;

import discult.modelapi.client.loaders.oldSMD.Socket;
import discult.modelapi.client.loaders.oldSMD.ValveStudioModel;
import discult.modelapi.client.models.ModelCustomWrapper;
import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.client.models.SMDModelRenderer;
import discult.modelapi.main.RenderLocation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GLSync;
import org.lwjgl.util.vector.Vector4f;

public class ModelTestingTwo extends ModelBase
{
   public ModelRenderer head = new ModelRenderer(this,0,0);

   public Vector4f position = new Vector4f(0,0,0,1);

   public ModelTestingTwo()
   {
      this.head.addBox(-4.0F,-4.0F,-4.0F, 8, 8, 8, 1);
      this.head.setRotationPoint(0.0F, 0, 0);
   }


   @Override
   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
   {
      super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

      GlStateManager.pushMatrix();

      GlStateManager.translate(position.x,position.y,position.z);

      this.head.render(scale);

      GlStateManager.popMatrix();


   }

   public void update(Vector4f pos)
   {
      this.position = pos;
   }

}
