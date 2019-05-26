package discult.modelapi.proxy;

import discult.modelapi.testing.EntityTest;
import discult.modelapi.testing.RenderTest;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
   public void postInit() {
      super.postInit();
      RenderingRegistry.registerEntityRenderingHandler(EntityTest.class, new RenderTest());
   }
}
