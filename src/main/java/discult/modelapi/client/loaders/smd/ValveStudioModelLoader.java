package discult.modelapi.client.loaders.smd;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class ValveStudioModelLoader implements ICustomModelLoader {
   public static final ValveStudioModelLoader instance = new ValveStudioModelLoader();

   public ValveStudioModelLoader() {
      ModelLoaderRegistry.registerLoader(this);
   }

   public void onResourceManagerReload(IResourceManager manager) {
   }

   public boolean accepts(ResourceLocation modelLocation) {
      return modelLocation.getResourcePath().endsWith(".qc");
   }

   public IModel loadModel(ResourceLocation modelLocation, boolean overrideSmoothShading) {
      try {
         return new ValveStudioModel(modelLocation, overrideSmoothShading);
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public IModel loadModel(ResourceLocation modelLocation) {
      try {
         return new ValveStudioModel(modelLocation);
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
