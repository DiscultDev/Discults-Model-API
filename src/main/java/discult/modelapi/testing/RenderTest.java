package discult.modelapi.testing;

import discult.modelapi.client.renderer.RenderSMDBase;
import net.minecraft.util.ResourceLocation;

public class RenderTest extends RenderSMDBase<EntityTest> {
   public RenderTest() {
      super(0.1F);
   }

   protected ResourceLocation getEntityTexture(EntityTest entity) {
      return new ResourceLocation("minecraft", "textures/blocks/cobblestone.png");
   }
}
