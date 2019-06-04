package discult.modelapi.main;

import discult.modelapi.client.loaders.oldSMD.ValveStudioModel;
import net.minecraft.util.ResourceLocation;

public class RenderLocation {
   private static ResourceLocation setMobModelLocation(String location, String mobname) {
      return new ResourceLocation("dmapi", "mobs/" + location + "/" + mobname + ".qc");
   }

   public static ValveStudioModel setMobModel(String location, String mobname) {
      ValveStudioModel model = null;

      try {
         model = new ValveStudioModel(setMobModelLocation(location, mobname));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return model;
   }
}
