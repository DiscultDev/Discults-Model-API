package discult.modelapi.client.loaders.oldSMD;

import discult.modelapi.client.loaders.general.IDiscultModel;
import discult.modelapi.common.util.SMDPatterns;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ValveStudioModel implements IModel, IDiscultModel {
   public SMDModel body;
   public HashMap<String, SMDAnimation> anims;
   protected Bone root;
   public ArrayList<Bone> allBones;
   public SMDAnimation currentAnimation;
   public ResourceLocation resource;
   public static boolean debugModel = false;
   private boolean hasAnimations;
   protected boolean usesMaterials;
   public boolean overrideSmoothShading;
   public boolean hasChanged;
   public int animations;

   public ValveStudioModel(ValveStudioModel model) {
      this.animations = 10;
      this.anims = new HashMap(this.animations);
      this.hasAnimations = false;
      this.usesMaterials = false;
      this.overrideSmoothShading = false;
      this.hasChanged = true;
      this.body = new SMDModel(model.body, this);
      Iterator var2 = model.anims.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         this.anims.put((String)entry.getKey(), new SMDAnimation((SMDAnimation)entry.getValue(), this));
      }

      this.hasAnimations = model.hasAnimations;
      this.usesMaterials = model.usesMaterials;
      this.resource = model.resource;
      this.currentAnimation = (SMDAnimation)this.anims.get("idle");
      this.overrideSmoothShading = model.overrideSmoothShading;
   }

   public ValveStudioModel(ResourceLocation resource, boolean overrideSmoothShading) throws Exception {
      this.animations = 10;
      this.anims = new HashMap(this.animations);
      this.hasAnimations = false;
      this.usesMaterials = false;
      this.overrideSmoothShading = false;
      this.hasChanged = true;
      this.overrideSmoothShading = overrideSmoothShading;
      this.resource = resource;
      this.loadQC(resource);
      this.reformBones();
      this.precalculateAnims();
   }

   public ValveStudioModel(ResourceLocation resource) throws Exception {
      this(resource, false);
   }

   private void loadQC(ResourceLocation resloc) throws Exception {
      String currentLine = null;
      int lineCount = 0;
      String[] bodyParams = null;
      ArrayList animParams = new ArrayList();

      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(resloc).getInputStream()));

         while((currentLine = reader.readLine()) != null) {
            ++lineCount;
            String[] params = SMDPatterns.MULTIPLE_WHITESPACE.split(currentLine);
            if (params[0].equalsIgnoreCase("$body")) {
               bodyParams = params;
            } else if (params[0].equalsIgnoreCase("$anim")) {
               if (this.anims == null) {
                  this.anims = new HashMap(this.animations);
               }

               this.hasAnimations = true;
               animParams.add(params);
            }
         }

         ResourceLocation modelPath = this.getResource(bodyParams[1]);
         this.body = new SMDModel(this, modelPath);
         Iterator var8 = animParams.iterator();

         while(var8.hasNext()) {
            String[] animPars = (String[])var8.next();
            String animName = animPars[1];
            ResourceLocation animPath = this.getResource(animPars[2]);
            this.anims.put(animName, new SMDAnimation(this, animName, animPath));
            if (animName.equalsIgnoreCase("idle")) {
               this.currentAnimation = (SMDAnimation)this.anims.get(animName);
            }
         }

      } catch (Exception var12) {
         throw new Exception("An error occurred reading the PQC file on line #" + lineCount, var12);
      }
   }

   public ResourceLocation getResource(String fileName) {
      String urlAsString = this.resource.getResourcePath();
      int lastIndex = urlAsString.lastIndexOf(47);
      String startString = urlAsString.substring(0, lastIndex);
      return new ResourceLocation(this.resource.getResourceDomain(), startString + "/" + fileName);
   }

   private void precalculateAnims() {
      Iterator var1 = this.anims.values().iterator();

      while(var1.hasNext()) {
         SMDAnimation anim = (SMDAnimation)var1.next();
         anim.precalculateAnimation(this.body);
      }

   }

   @SideOnly(Side.CLIENT)
   @Override
   public void renderAll() {
      Tessellator tess = Tessellator.getInstance();
      body.render(tess.getBuffer(), overrideSmoothShading);
   }

   void sendBoneData(SMDModel model) {
      this.allBones = model.bones;
      if (!model.isBodyGroupPart) {
         this.root = model.root;
      }

   }

   private void reformBones() {
      this.root.reformChildren();
      this.allBones.forEach(Bone::invertRestMatrix);
   }

   public void animate() {
      this.resetVerts(this.body);
      if (this.body.currentAnim == null) {
         this.setAnimation("idle");
      }

      this.root.setModified();
      this.allBones.forEach(Bone::applyModified);
      this.applyVertChange(this.body);
      this.hasChanged = true;
   }

   private void resetVerts(SMDModel model) {
      if (model != null) {
         model.verts.forEach(DeformVertex::reset);
      }

   }

   private void applyVertChange(SMDModel model) {
      if (model != null) {
         model.verts.forEach(DeformVertex::applyChange);
      }

   }

   public void setAnimation(String animname) {
      if (this.anims.containsKey(animname)) {
         this.currentAnimation = (SMDAnimation)this.anims.get(animname);
      } else {
         this.currentAnimation = (SMDAnimation)this.anims.get("idle");
      }

      this.body.setAnimation(this.currentAnimation);
   }

   public static void print(Object o) {
      if (debugModel) {
         System.out.println(o);
      }

   }

   public boolean hasAnimations() {
      return this.hasAnimations;
   }

   public IBakedModel bake(IModelState state, VertexFormat format, Function bakedTextureGetter) {
      return null;
   }
}
