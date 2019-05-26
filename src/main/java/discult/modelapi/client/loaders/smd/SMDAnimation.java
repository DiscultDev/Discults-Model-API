package discult.modelapi.client.loaders.smd;

import discult.modelapi.common.util.SMDPatterns;
import discult.modelapi.common.util.helpers.VectorHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Matrix4f;

public class SMDAnimation {
   public final ValveStudioModel owner;
   public ArrayList<AnimationFrame> frames = new ArrayList();
   public ArrayList<Bone> bones = new ArrayList();
   public int currentFrameIndex = 0;
   public int lastFrameIndex;
   public int totalFrames;
   public String animationName;
   private int frameIDBank = 0;

   public SMDAnimation(ValveStudioModel owner, String animationName, ResourceLocation resloc) throws Exception {
      this.owner = owner;
      this.animationName = animationName;
      this.loadSmdAnim(resloc);
      this.setBoneChildren();
      this.reform();
   }

   public SMDAnimation(SMDAnimation anim, ValveStudioModel owner) {
      this.owner = owner;
      this.animationName = anim.animationName;
      Iterator var3 = anim.bones.iterator();

      while(var3.hasNext()) {
         Bone b = (Bone)var3.next();
         this.bones.add(new Bone(b, b.parent != null ? (Bone)this.bones.get(b.parent.ID) : null, (SMDModel)null));
      }

      this.frames.addAll((Collection)anim.frames.stream().map((f) -> {
         return new AnimationFrame(f, this);
      }).collect(Collectors.toList()));
      this.totalFrames = anim.totalFrames;
   }

   private void loadSmdAnim(ResourceLocation resloc) throws Exception {
      BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(resloc).getInputStream()));
      String currentLine = null;
      int lineCount = 0;

      try {
         while(true) {
            while(true) {
               do {
                  if ((currentLine = reader.readLine()) == null) {
                     return;
                  }

                  ++lineCount;
               } while(currentLine.startsWith("version"));

               if (currentLine.startsWith("nodes")) {
                  ++lineCount;

                  while(!(currentLine = reader.readLine()).startsWith("end")) {
                     ++lineCount;
                     this.parseBone(currentLine, lineCount);
                  }
               } else if (currentLine.startsWith("skeleton")) {
                  this.startParsingAnimation(reader, lineCount, resloc);
               }
            }
         }
      } catch (IOException var6) {
         if (lineCount == -1) {
            throw new Exception("there was a problem opening the model file : " + resloc, var6);
         } else {
            throw new Exception("an error occurred reading the SMD file \"" + resloc + "\" on line #" + lineCount, var6);
         }
      }
   }

   private void parseBone(String line, int lineCount) {
      String[] params = line.split("\"");
      int id = Integer.parseInt(SMDPatterns.SPACE.matcher(params[0]).replaceAll(""));
      String boneName = params[1];
      int parentID = Integer.parseInt(SMDPatterns.SPACE.matcher(params[2]).replaceAll(""));
      Bone parent = parentID >= 0 ? (Bone)this.bones.get(parentID) : null;
      this.bones.add(id, new Bone(boneName, id, parent, (SMDModel)null));
      ValveStudioModel.print(boneName);
   }

   private void startParsingAnimation(BufferedReader reader, int count, ResourceLocation resloc) throws Exception {
      int currentTime = 0;
      int lineCount = count + 1;
      String currentLine = null;

      try {
         while(true) {
            while((currentLine = reader.readLine()) != null) {
               ++lineCount;
               String[] params = SMDPatterns.MULTIPLE_WHITESPACE.split(currentLine);
               if (params[0].equalsIgnoreCase("time")) {
                  currentTime = Integer.parseInt(params[1]);
                  this.frames.add(currentTime, new AnimationFrame(this));
               } else {
                  if (currentLine.startsWith("end")) {
                     this.totalFrames = this.frames.size();
                     ValveStudioModel.print("Total number of frames = " + this.totalFrames);
                     return;
                  }

                  int boneIndex = Integer.parseInt(params[0]);
                  float[] locRots = new float[6];
                  Bone rotBone = (Bone)this.bones.get(boneIndex);

                  for(int i = 1; i < 7; ++i) {
                     locRots[i - 1] = Float.parseFloat(params[i]);
                  }

                  rotBone.setX(locRots[0]);
                  rotBone.setY(locRots[1]);
                  rotBone.setZ(locRots[2]);
                  rotBone.setXR(locRots[3]);
                  rotBone.setYR(locRots[4]);
                  rotBone.setZR(locRots[5]);
                  Matrix4f animated = VectorHelper.matrix4FromLocRot(locRots[0], -locRots[1], -locRots[2], locRots[3], -locRots[4], -locRots[5]);
                  ((AnimationFrame)this.frames.get(currentTime)).addTransforms(boneIndex, animated);
               }
            }

            return;
         }
      } catch (Exception var12) {
         throw new Exception("an error occurred reading the SMD file \"" + resloc + "\" on line #" + lineCount, var12);
      }
   }

   public int requestFrameID() {
      int result = this.frameIDBank++;
      return result;
   }

   private void setBoneChildren() {
      for(int i = 0; i < this.bones.size(); ++i) {
         Bone theBone = (Bone)this.bones.get(i);
         this.bones.stream().filter((child) -> {
            return child.parent == theBone;
         }).forEach(theBone::addChild);
      }

   }

   public void reform() {
      int rootID = this.owner.body.root.ID;
      Iterator var2 = this.frames.iterator();

      while(var2.hasNext()) {
         AnimationFrame frame = (AnimationFrame)var2.next();
         frame.fixUp(rootID, 0.0F);
         frame.reform();
      }

   }

   public void precalculateAnimation(SMDModel model) {
      Iterator var2 = this.frames.iterator();

      while(var2.hasNext()) {
         AnimationFrame frame1 = (AnimationFrame)var2.next();
         model.resetVerts();
         AnimationFrame frame = frame1;

         for(int j = 0; j < model.bones.size(); ++j) {
            Bone bone = (Bone)model.bones.get(j);
            Matrix4f animated = (Matrix4f)frame.transforms.get(j);
            bone.preloadAnimation(frame, animated);
         }
      }

   }

   public int getNumFrames() {
      return this.frames.size();
   }

   public void setCurrentFrame(int i) {
      if (this.lastFrameIndex != i) {
         this.currentFrameIndex = i;
         this.lastFrameIndex = i;
      }

   }
}
