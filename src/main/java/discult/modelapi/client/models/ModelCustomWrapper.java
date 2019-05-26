package discult.modelapi.client.models;

import discult.modelapi.client.loaders.general.IDiscultModel;
import net.minecraft.client.renderer.GlStateManager;

public class ModelCustomWrapper {
   public IDiscultModel model;
   int frame = 0;
   float offsetX = 0.0F;
   float offsetY = 0.0F;
   float offsetZ = 0.0F;

   public ModelCustomWrapper(IDiscultModel m) {
      this.model = m;
   }

   public ModelCustomWrapper(IDiscultModel m, float x, float y, float z) {
      this.model = m;
      this.offsetX = x;
      this.offsetY = y;
      this.offsetZ = z;
   }

   public ModelCustomWrapper setOffsets(float x, float y, float z) {
      this.offsetX = x;
      this.offsetY = y;
      this.offsetZ = z;
      return this;
   }

   public void render(float scale) {
      GlStateManager.pushMatrix();
      GlStateManager.scale(scale, scale, scale);
      this.model.renderAll();
      GlStateManager.popMatrix();
   }

   public void renderOffset(float scale) {
      GlStateManager.pushMatrix();
      GlStateManager.scale(scale, scale, scale);
      GlStateManager.translate(this.offsetX, this.offsetZ, this.offsetY);
      this.model.renderAll();
   }
}
