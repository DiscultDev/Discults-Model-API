package discult.modelapi.main;

import discult.modelapi.proxy.CommonProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(
   modid = "dmapi",
   name = "Discult's Modding API",
   version = "0.1.0",
   acceptedMinecraftVersions = "[1.12.2]"
)
public class DiscultModelAPI {
   @SidedProxy(
      clientSide = "discult.modelapi.proxy.ClientProxy",
      serverSide = "discult.modelapi.proxy.CommonProxy"
   )
   public static CommonProxy proxy;

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
    //  EntityRegistry.registerModEntity(new ResourceLocation("dmapi", "test"), EntityTest.class, "test", 999, this, 64, 1, true);
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      proxy.postInit();
   }
}
