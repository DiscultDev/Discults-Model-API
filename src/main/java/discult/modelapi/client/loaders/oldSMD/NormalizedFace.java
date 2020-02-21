package discult.modelapi.client.loaders.oldSMD;

import discult.modelapi.client.loaders.general.Vertex;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel.TextureCoordinate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class NormalizedFace {
   public DeformVertex[] vertices;
   public TextureCoordinate[] textureCoordinates;
   public Vertex faceNormal;

   public NormalizedFace(DeformVertex[] xyz, TextureCoordinate[] uvs) {
      this.vertices = xyz;
      this.textureCoordinates = uvs;
   }

   public NormalizedFace(NormalizedFace face, ArrayList verts) {
      this.vertices = new DeformVertex[face.vertices.length];

      for(int i = 0; i < this.vertices.length; ++i) {
         this.vertices[i] = (DeformVertex)verts.get(face.vertices[i].ID);
      }

      this.textureCoordinates = new TextureCoordinate[face.textureCoordinates.length];
      System.arraycopy(face.textureCoordinates, 0, this.textureCoordinates, 0, this.textureCoordinates.length);
      if (face.faceNormal != null) {
         this.faceNormal = face.faceNormal;
      }

   }


   @SideOnly(Side.CLIENT)
   public void render(BufferBuilder builder, boolean smoothShading)
   {
      if(faceNormal == null)
         faceNormal = calculateFaceNormal();


      builder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);


      for(int index = 0; index < vertices.length; ++index) {
         //this means that it stores normal data so will need to check if it is to be smoothed or not.
         if (vertices[index] instanceof DeformVertex && smoothShading) {
            builder.pos(vertices[index].getX(), vertices[index].getY(), vertices[index].getZ())
                    .tex(textureCoordinates[index].u, textureCoordinates[index].v)
                    .normal(((DeformVertex) vertices[index]).getXn(), ((DeformVertex) vertices[index]).getYn(),
                            ((DeformVertex) vertices[index]).getZn()).endVertex();
         } else {
            builder.pos(vertices[index].getX(), vertices[index].getY(), vertices[index].getZ())
                    .tex(textureCoordinates[index].u, textureCoordinates[index].v)
                    .normal(faceNormal.getX(), faceNormal.getY(),
                            faceNormal.getZ()).endVertex();
         }
      }

        /*
        Minecraft Models work in quads so Buffer builder only supports quads, so to get around this if model has
        triangle faces is to add the starting vert again at the end.
         */
      if(vertices.length == 3)
      {
         if(vertices[0] instanceof DeformVertex && smoothShading)
         {
            builder.pos(vertices[0].getX(), vertices[0].getY(), vertices[0].getZ())
                    .tex(textureCoordinates[0].u, textureCoordinates[0].v)
                    .normal(((DeformVertex)vertices[0]).getXn(), ((DeformVertex)vertices[0]).getYn(),
                            ((DeformVertex)vertices[0]).getZn()).endVertex();
         }
         else
         {
            builder.pos(vertices[0].getX(), vertices[0].getY(), vertices[0].getZ())
                    .tex(textureCoordinates[0].u, textureCoordinates[0].v)
                    .normal(faceNormal.getX(), faceNormal.getY(),
                            faceNormal.getZ()).endVertex();
         }
      }

      Tessellator.getInstance().draw();//draw the model to screen.
   }

   public Vertex calculateFaceNormal() {
      Vec3d v1 = new Vec3d(vertices[1].getX() - vertices[0].getX(), vertices[1].getY() - vertices[0].getY(), vertices[1].getZ() - vertices[0].getZ());
      Vec3d v2 = new Vec3d(vertices[2].getX() - vertices[0].getX(), vertices[2].getY() - vertices[0].getY(), vertices[2].getZ() - vertices[0].getZ());
      Vec3d normalVector = null;

      normalVector = v1.crossProduct(v2).normalize();

      return new Vertex((float) normalVector.x, (float) normalVector.y, (float) normalVector.z);
   }

}
