package discult.modelapi.client.loaders.oldSMD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import discult.modelapi.utils.Quaternion;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Bone {
   public Bone copy = null;
   public String name;
   public int ID;
   public Bone parent;
   public SMDModel owner;
   private Boolean isDummy;
   public Matrix4f rest;
   public Matrix4f restInverted;
   public Matrix4f modified = new Matrix4f();
   public Matrix4f prevInverted = new Matrix4f();
   public ArrayList<Bone> children = new ArrayList();
   public HashMap<DeformVertex, Float> verts = new HashMap();
   public HashMap<String, HashMap<Integer, Matrix4f>>  animatedTransforms = new HashMap();
   public HashMap<String, HashMap<Integer, BonePos>> bonePos = new HashMap<>();
   private float X;
   private float Y;
   private float Z;
   private float NZ;
   private float XR;
   private float YR;
   private float ZR;

   public Bone(String name, int ID, Bone parent, SMDModel owner) {
      this.name = name;
      this.ID = ID;
      this.parent = parent;
      this.owner = owner;
   }

   public Bone(Bone bone, Bone parent, SMDModel owner) {
      this.name = bone.name;
      this.ID = bone.ID;
      this.owner = owner;
      this.parent = parent;

      for(Map.Entry<DeformVertex, Float> entry : bone.verts.entrySet()) {
            this.verts.put((DeformVertex) owner.verts.get((entry.getKey()).ID), entry.getValue());
      }

      this.animatedTransforms = new HashMap(bone.animatedTransforms);
      this.restInverted = bone.restInverted;
      this.rest = bone.rest;
      bone.copy = this;
   }

   public String getBoneByName(String name) {
      if (this.name == name) {
         return this.name;
      } else {
         System.out.println(name + " Does not excest");
         return null;
      }
   }

   public void setChildren(Bone bone, ArrayList bones) {
      for(int i = 0; i < bone.children.size(); ++i) {
         Bone child = (Bone)bone.children.get(i);
         this.children.add((Bone)bones.get(child.ID));
         ((Bone)bones.get(child.ID)).parent = this;
      }

   }

   public boolean isDummy() {
      return this.isDummy == null ? (this.isDummy = this.parent == null && this.children.isEmpty()) : this.isDummy;
   }

   public void setRest(Matrix4f resting) {
      this.rest = resting;
   }

   public void addChild(Bone child) {
      this.children.add(child);
   }

   public void addVertex(DeformVertex vertex, float weight) {
      if (this.name.equals("blender_implicit")) {
         throw new UnsupportedOperationException("NO.");
      } else {
         this.verts.put(vertex, weight);
      }
   }

   private void reform(Matrix4f parentMatrix) {
      this.rest = Matrix4f.mul(parentMatrix, this.rest, (Matrix4f)null);
      if (ValveStudioModel.debugModel) {
         System.out.println(this.name + ' ' + this.parent);
      }

      this.reformChildren();
   }

   public void reformChildren() {
      Iterator var1 = this.children.iterator();

      while(var1.hasNext()) {
         Bone child = (Bone)var1.next();
         child.reform(this.rest);
      }

   }

   public void invertRestMatrix() {
      this.restInverted = Matrix4f.invert(this.rest, (Matrix4f)null);
   }

   public void reset() {
      this.modified.setIdentity();
   }

   public void preloadAnimation(AnimationFrame key, Matrix4f animated) {
      HashMap precalcArray;
      if (this.animatedTransforms.containsKey(key.owner.animationName)) {
         precalcArray = (HashMap)this.animatedTransforms.get(key.owner.animationName);
      } else {
         precalcArray = new HashMap();
      }

      precalcArray.put(key.ID, animated);
      this.animatedTransforms.put(key.owner.animationName, precalcArray);
   }

   public void setModified() {
      Matrix4f realInverted;
      Matrix4f real;
      if (this.owner.owner.hasAnimations() && this.owner.currentAnim != null) {
         AnimationFrame currentFrame = (AnimationFrame)this.owner.currentAnim.frames.get(this.owner.currentAnim.currentFrameIndex);
         realInverted = (Matrix4f)currentFrame.transforms.get(this.ID);
         real = (Matrix4f)currentFrame.invertTransforms.get(this.ID);
      } else {
         realInverted = this.rest;
         real = this.restInverted;
      }

      Matrix4f delta = new Matrix4f();
      Matrix4f absolute = new Matrix4f();
      Matrix4f.mul(realInverted, real, delta);
      this.modified = this.parent != null ? Matrix4f.mul(this.parent.modified, delta, this.initModified()) : delta;
      Matrix4f.mul(real, this.modified, absolute);
      Matrix4f.invert(absolute, this.prevInverted);
      this.children.forEach(Bone::setModified);
   }

   protected Matrix4f initModified() {
      return this.modified == null ? (this.modified = new Matrix4f()) : this.modified;
   }

   public void applyModified() {
      AnimationFrame currentFrame = this.owner.currentFrame();
      if (currentFrame != null) {
         HashMap<Integer, Matrix4f> precalcArray = this.animatedTransforms.get(currentFrame.owner.animationName);
         Matrix4f animated = (Matrix4f)precalcArray.get(currentFrame.ID);
         Matrix4f animatedChange = new Matrix4f();
         Matrix4f.mul(animated, this.restInverted, animatedChange);
         this.modified = this.modified == null ? animatedChange : Matrix4f.mul(this.modified, animatedChange, this.modified);
      }

      this.verts.entrySet().forEach((entry) -> {
         ((DeformVertex)entry.getKey()).applyModified(this, entry.getValue());
      });
      this.reset();
   }

   public Vector3f getPosition()
   {
     // AnimationFrame currentFrame = this.owner.currentFrame();
     // HashMap<Integer, Matrix4f> precalcArray = this.animatedTransforms.get(currentFrame.owner.animationName);
     // Matrix4f animation = precalcArray.get(currentFrame.ID);
    return new Vector3f(modified.m03, modified.m13, modified.m23);
   }

   public Quaternion getRotation()
   {
      //AnimationFrame currentFrame = this.owner.currentFrame();
     // HashMap<Integer, Matrix4f> precalcArray = this.animatedTransforms.get(currentFrame.owner.animationName);
     // Matrix4f animation = precalcArray.get(currentFrame.ID);
      return Quaternion.fromMatrix(modified);
   }
}
