package discult.modelapi.client.models;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class SMDModelRenderer extends ModelRenderer {
   private int displayList;
   private boolean compiled = false;
   public ArrayList objs = new ArrayList();
   private boolean isTransparent = false;
   private float transparency;

   public SMDModelRenderer(ModelBase par1ModelBase, String par2Str) {
      super(par1ModelBase, par2Str);
   }

   public SMDModelRenderer(ModelBase par1ModelBase) {
      super(par1ModelBase);
   }

   public SMDModelRenderer(ModelBase par1ModelBase, int par2, int par3) {
      super(par1ModelBase, par2, par3);
   }

   public void addCustomModel(ModelCustomWrapper model) {
      this.objs.add(model);
   }

   public void setTransparent(float transparency) {
      this.isTransparent = true;
      this.transparency = transparency;
   }

   @SideOnly(Side.CLIENT)
   public void render(float par1) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(par1);
         }

         GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
         int var2;
         if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
               GlStateManager.callList(this.displayList);
               this.renderCustomModels(par1);
               if (this.childModels != null) {
                  for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                     ((ModelRenderer)this.childModels.get(var2)).render(par1);
                  }
               }
            } else {
               GlStateManager.translate(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
               GlStateManager.callList(this.displayList);
               this.renderCustomModels(par1);
               if (this.childModels != null) {
                  for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                     ((ModelRenderer)this.childModels.get(var2)).render(par1);
                  }
               }

               GlStateManager.translate(-this.rotationPointX * par1, -this.rotationPointY * par1, -this.rotationPointZ * par1);
            }
         } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
            if (this.rotateAngleY != 0.0F) {
               GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleZ != 0.0F) {
               GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleX != 0.0F) {
               GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GlStateManager.callList(this.displayList);
            this.renderCustomModels(par1);
            if (this.childModels != null) {
               for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                  ((ModelRenderer)this.childModels.get(var2)).render(par1);
               }
            }

            GlStateManager.popMatrix();
         }

         GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
      }

   }

   @SideOnly(Side.CLIENT)
   private void compileDisplayList(float par1) {
      this.displayList = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.displayList, 4864);
      Tessellator tessellator = Tessellator.getInstance();
      Iterator var3 = this.cubeList.iterator();

      while(var3.hasNext()) {
         Object aCubeList = var3.next();
         ((ModelBox)aCubeList).render(tessellator.getBuffer(), par1);
      }

      GL11.glEndList();
      this.compiled = true;
   }

   protected void renderCustomModels(float scale) {
      if (this.isTransparent) {
         GlStateManager.enableBlend();
         GlStateManager.depthMask(false);
         GlStateManager.color(1.0F, 1.0F, 1.0F, this.transparency);
      }

      Iterator var2 = this.objs.iterator();

      while(var2.hasNext()) {
         ModelCustomWrapper obj = (ModelCustomWrapper)var2.next();
         obj.render(scale);
      }

      if (this.isTransparent) {
         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (OpenGlHelper.useVbo()) {
         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
      }

   }
}
