package discult.modelapi.client.loaders.oldSMD;

import discult.modelapi.common.util.SMDPatterns;
import discult.modelapi.common.util.helpers.CommonHelper;
import discult.modelapi.common.util.helpers.VectorHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel.TextureCoordinate;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.GL11;

public class SMDModel {
   public final ValveStudioModel owner;
   public ArrayList<NormalizedFace> faces = new ArrayList<>(0);
   public ArrayList<DeformVertex> verts = new ArrayList<>(0);
   public ArrayList<Bone> bones = new ArrayList(0);
   public HashMap nameToBoneMapping = new HashMap();
   public SMDAnimation currentAnim;
   public String fileName;
   private int vertexIDBank = 0;
   protected boolean isBodyGroupPart;
   int lineCount = -1;
   public Bone root;
   public int vertexVbo = -1;
   public int textureVbo = -1;
   public int normalsVbo = -1;
   private FloatBuffer vertexBuffer;
   private FloatBuffer normalBuffer;
   public static boolean isSmoothModel = false;

   public SMDModel(SMDModel model, ValveStudioModel owner) {
      this.owner = owner;
      this.isBodyGroupPart = model.isBodyGroupPart;
      Iterator var3 = model.faces.iterator();

      while(var3.hasNext()) {
         NormalizedFace face = (NormalizedFace)var3.next();
         DeformVertex[] vertices = new DeformVertex[face.vertices.length];

         for(int i = 0; i < vertices.length; ++i) {
            DeformVertex d = new DeformVertex(face.vertices[i]);
            CommonHelper.ensureIndex(this.verts, d.ID);
            this.verts.set(d.ID, d);
         }
      }

      this.faces.addAll((Collection)model.faces.stream().map((face) -> {
         return new NormalizedFace(face, this.verts);
      }).collect(Collectors.toList()));

      int i;
      Bone b;
      for(i = 0; i < model.bones.size(); ++i) {
         b = (Bone)model.bones.get(i);
         this.bones.add(new Bone(b, (Bone)null, this));
      }

      for(i = 0; i < model.bones.size(); ++i) {
         b = (Bone)model.bones.get(i);
         b.copy.setChildren(b, this.bones);
      }

      this.root = model.root.copy;
      owner.sendBoneData(this);
   }

   public SMDModel(ValveStudioModel owner, ResourceLocation resloc) throws Exception {
      this.owner = owner;
      this.isBodyGroupPart = false;
      this.loadSmdModel(resloc, (SMDModel)null);
      this.setBoneChildren();
      this.determineRoot();
      owner.sendBoneData(this);
      ValveStudioModel.print("Number of vertices = " + this.verts.size());
   }

   public SMDModel(ValveStudioModel owner, ResourceLocation resloc, SMDModel body) throws Exception {
      this.owner = owner;
      this.isBodyGroupPart = true;
      this.loadSmdModel(resloc, body);
      this.setBoneChildren();
      this.determineRoot();
      owner.sendBoneData(this);
   }

   private void loadSmdModel(ResourceLocation resloc, SMDModel body) throws Exception {
      BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(resloc).getInputStream()));
      String currentLine = null;

      try {
         this.lineCount = 0;

         while((currentLine = reader.readLine()) != null) {
            ++this.lineCount;
            if (!currentLine.startsWith("version")) {
               if (currentLine.startsWith("nodes")) {
                  ++this.lineCount;

                  while(!(currentLine = reader.readLine()).startsWith("end")) {
                     ++this.lineCount;
                     this.parseBone(currentLine, this.lineCount, body);
                  }

                  ValveStudioModel.print("Number of model bones = " + this.bones.size());
               } else if (currentLine.startsWith("skeleton")) {
                  ++this.lineCount;
                  reader.readLine();
                  ++this.lineCount;

                  while(!(currentLine = reader.readLine()).startsWith("end")) {
                     ++this.lineCount;
                     if (!this.isBodyGroupPart) {
                        this.parseBoneValues(currentLine, this.lineCount);
                     }
                  }
               } else if (currentLine.startsWith("triangles")) {
                  ++this.lineCount;

                  while(!reader.readLine().startsWith("end")) {
                     String[] params = new String[3];

                     for(int i = 0; i < 3; ++i) {
                        ++this.lineCount;
                        params[i] = reader.readLine();
                     }

                     this.parseFace(params, this.lineCount);
                  }
               }
            }
         }
      } catch (Exception var7) {
         if (this.lineCount == -1) {
            throw new Exception("there was a problem opening the model file : " + resloc, var7);
         }

         throw new Exception("an error occurred reading the SMD file \"" + resloc + "\" on line #" + this.lineCount, var7);
      }

      ValveStudioModel.print("Number of faces = " + this.faces.size());
   }

   private void parseBone(String line, int lineCount, SMDModel body) {
      String[] params = line.split("\"");
      int id = Integer.parseInt(SMDPatterns.SPACE.matcher(params[0]).replaceAll(""));
      String boneName = params[1];
      Bone theBone = body != null ? body.getBoneByName(boneName) : null;
      if (theBone == null) {
         int parentID = Integer.parseInt(SMDPatterns.SPACE.matcher(params[2]).replaceAll(""));
         Bone parent = parentID >= 0 ? (Bone)this.bones.get(parentID) : null;
         theBone = new Bone(boneName, id, parent, this);
      }

      CommonHelper.ensureIndex(this.bones, id);
      this.bones.set(id, theBone);
      this.nameToBoneMapping.put(boneName, theBone);
      ValveStudioModel.print(boneName);
   }

   private void parseBoneValues(String line, int lineCount) {
      String[] params = SMDPatterns.MULTIPLE_WHITESPACE.split(line);
      int id = Integer.parseInt(params[0]);
      float[] locRots = new float[6];

      for(int i = 1; i < 7; ++i) {
         locRots[i - 1] = Float.parseFloat(params[i]);
      }

      Bone theBone = (Bone)this.bones.get(id);
      theBone.setRest(VectorHelper.matrix4FromLocRot(locRots[0], -locRots[1], -locRots[2], locRots[3], -locRots[4], -locRots[5]));
   }

   private void parseFace(String[] params, int lineCount) {
      DeformVertex[] faceVerts = new DeformVertex[3];
      TextureCoordinate[] uvs = new TextureCoordinate[3];

      for(int i = 0; i < 3; ++i) {
         String[] values = SMDPatterns.MULTIPLE_WHITESPACE.split(params[i]);
         float x = Float.parseFloat(values[1]);
         float y = -Float.parseFloat(values[2]);
         float z = -Float.parseFloat(values[3]);
         float xn = Float.parseFloat(values[4]);
         float yn = -Float.parseFloat(values[5]);
         float zn = -Float.parseFloat(values[6]);
         DeformVertex v = this.getExisting(x, y, z);
         if (v == null) {
            faceVerts[i] = new DeformVertex(x, y, z, xn, yn, zn, this.vertexIDBank);
            CommonHelper.ensureIndex(this.verts, this.vertexIDBank);
            this.verts.set(this.vertexIDBank, faceVerts[i]);
            ++this.vertexIDBank;
         } else {
            faceVerts[i] = v;
         }

         uvs[i] = new TextureCoordinate(Float.parseFloat(values[7]), 1.0F - Float.parseFloat(values[8]), 0.0F);
         if (values.length > 10) {
            this.doBoneWeights(values, faceVerts[i]);
         }
      }

      NormalizedFace face = new NormalizedFace(faceVerts, uvs);
      face.vertices = faceVerts;
      face.textureCoordinates = uvs;
      this.faces.add(face);
   }

   private DeformVertex getExisting(float x, float y, float z) {
      Iterator var4 = this.verts.iterator();

      DeformVertex v;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         v = (DeformVertex)var4.next();
      } while(!v.equals(x, y, z));

      return v;
   }

   private void doBoneWeights(String[] values, DeformVertex vert) {
      int links = Integer.parseInt(values[9]);
      float[] weights = new float[links];
      float sum = 0.0F;

      int i;
      for(i = 0; i < links; ++i) {
         weights[i] = Float.parseFloat(values[i * 2 + 11]);
         sum += weights[i];
      }

      for(i = 0; i < links; ++i) {
         int boneID = Integer.parseInt(values[i * 2 + 10]);
         float weight = weights[i] / sum;
         ((Bone)this.bones.get(boneID)).addVertex(vert, weight);
      }

   }

   private void setBoneChildren() {
      for(int i = 0; i < this.bones.size(); ++i) {
         Bone theBone = (Bone)this.bones.get(i);
         this.bones.stream().filter((child) -> {
            return child.parent == theBone;
         }).forEach(theBone::addChild);
      }

   }

   private void determineRoot() {
      Iterator var1 = this.bones.iterator();

      Bone b;
      while(var1.hasNext()) {
         b = (Bone)var1.next();
         if (b.parent == null && !b.children.isEmpty()) {
            this.root = b;
            break;
         }
      }

      if (this.root == null) {
         var1 = this.bones.iterator();

         while(var1.hasNext()) {
            b = (Bone)var1.next();
            if (!b.name.equals("blender_implicit")) {
               this.root = b;
               break;
            }
         }
      }

   }

   public void setAnimation(SMDAnimation anim) {
      this.currentAnim = anim;
   }

   public Bone getBoneByID(int id) {
      try {
         return (Bone)this.bones.get(id);
      } catch (IndexOutOfBoundsException var3) {
         return null;
      }
   }

   public Bone getBoneByName(String name) {
      Iterator var2 = this.bones.iterator();

      Bone b;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         b = (Bone)var2.next();
      } while(!b.name.equals(name));

      return b;
   }

   public AnimationFrame currentFrame() {
      return this.currentAnim == null ? null : (this.currentAnim.frames == null ? null : (this.currentAnim.frames.isEmpty() ? null : (AnimationFrame)this.currentAnim.frames.get(this.currentAnim.currentFrameIndex)));
   }

   public void resetVerts() {
      this.verts.forEach(DeformVertex::reset);
   }

   public void render(boolean hasChanged) {
      if (this.owner.overrideSmoothShading) {
         isSmoothModel = false;
      }

      boolean useVBO = OpenGlHelper.useVbo();
      if (!useVBO) {
         GL11.glBegin(4);
         Iterator var3 = this.faces.iterator();

         while(var3.hasNext()) {
            NormalizedFace f = (NormalizedFace)var3.next();
            f.addFaceForRender(isSmoothModel);
         }

         GL11.glEnd();
      } else {
         if (hasChanged) {
            if (this.vertexVbo == -1) {
               this.vertexVbo = ARBBufferObject.glGenBuffersARB();
               this.textureVbo = ARBBufferObject.glGenBuffersARB();
               this.normalsVbo = ARBBufferObject.glGenBuffersARB();
               this.buildVBO(isSmoothModel);
            } else {
               this.updateVBO(isSmoothModel);
            }
         }

         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.vertexVbo);
         GL11.glVertexPointer(3, 5126, 0, 0L);
         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.textureVbo);
         GL11.glTexCoordPointer(2, 5126, 0, 0L);
         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.normalsVbo);
         GL11.glNormalPointer(5126, 0, 0L);
         GL11.glEnableClientState(32884);
         GL11.glEnableClientState(32888);
         GL11.glEnableClientState(32885);
         GL11.glDrawArrays(4, 0, this.faces.size() * 3);
         GL11.glDisableClientState(32884);
         GL11.glDisableClientState(32888);
         GL11.glDisableClientState(32885);
         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
      }

   }

   private void updateVBO(boolean smoothShading) {
      this.vertexBuffer.clear();
      this.normalBuffer.clear();
      Iterator var2 = this.faces.iterator();

      while(var2.hasNext()) {
         NormalizedFace face = (NormalizedFace)var2.next();
         face.addFaceForRender(this.vertexBuffer, this.normalBuffer, smoothShading);
      }

      this.vertexBuffer.flip();
      OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.vertexVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.GL_ARRAY_BUFFER, this.vertexBuffer, 35044);
      this.normalBuffer.flip();
      OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.normalsVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.GL_ARRAY_BUFFER, this.normalBuffer, 35044);
   }

   private void buildVBO(boolean smoothShading) {
      this.vertexBuffer = BufferUtils.createFloatBuffer(this.faces.size() * 9);
      FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(this.faces.size() * 6);
      this.normalBuffer = BufferUtils.createFloatBuffer(this.faces.size() * 9);
      Iterator var3 = this.faces.iterator();

      while(var3.hasNext()) {
         NormalizedFace face = (NormalizedFace)var3.next();
         face.addFaceForRender(this.vertexBuffer, textureBuffer, this.normalBuffer, smoothShading);
      }

      this.vertexBuffer.flip();
      OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.vertexVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.GL_ARRAY_BUFFER, this.vertexBuffer, 35044);
      textureBuffer.flip();
      OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.textureVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.GL_ARRAY_BUFFER, textureBuffer, 35044);
      this.normalBuffer.flip();
      OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, this.normalsVbo);
      ARBBufferObject.glBufferDataARB(OpenGlHelper.GL_ARRAY_BUFFER, this.normalBuffer, 35044);
   }
}
