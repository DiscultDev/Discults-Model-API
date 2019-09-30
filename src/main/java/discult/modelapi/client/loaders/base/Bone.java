package discult.modelapi.client.loaders.base;

import discult.modelapi.ModelAPI;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Bone/Joint class this represents a logical bone for the model
 * and will be used with models that can be manipulated.
 * This class will handle information and work as a base for the 
 * vertices to calculate what their current position should be during 
 * a certain frame.
 */
public class Bone 
{

	public String name;
	public int ID;
	public Bone parent;
	public Skeleton owner;

	public Matrix4f restTransform;
	public Matrix4f restInverted;
    public Matrix4f prevInverted = new Matrix4f();
	public Matrix4f transform = new Matrix4f();
	public List<Bone> children = new ArrayList<>();

    public HashMap<TransformVertex, Float> verts = new HashMap<>();
    public HashMap<String, HashMap<Integer, Matrix4f>>  animatedTransforms = new HashMap();

    public Bone(String name, int ID, Bone parent, Skeleton owner)
    {
        this.name = name;
        this.ID = ID;
        this.parent = parent;
        this.owner = owner;
    }

    public void addChild(Bone bone)
    {
        children.add(bone);
    }

    public void addVertex(TransformVertex vertex, float weight) {
        if (name.equals("blender_implicit")) {
            throw new UnsupportedOperationException("NO.");
        } else {
            verts.put(vertex, weight);
        }
    }


    public void preloadAnimation(Frame key, Matrix4f animated) {
        HashMap precalcArray;
        if (this.animatedTransforms.containsKey(key.owner.name)) {
            precalcArray = (HashMap)this.animatedTransforms.get(key.owner.name);
        } else {
            precalcArray = new HashMap();
        }

        precalcArray.put(key.ID, animated);
        this.animatedTransforms.put(key.owner.name, precalcArray);
    }

    public void reCalc(Matrix4f parentMatrix)
    {
        restTransform = Matrix4f.mul(parentMatrix, restTransform, null);

        reCalcChildren();
    }

    public void reCalcChildren()
    {
        children.forEach(bone -> {bone.reCalc(restTransform);});
    }

    public void invertRestTransform()
    {
        restInverted = Matrix4f.invert(restTransform, null);
    }

    public void resetTransform()
    {
        this.transform.setIdentity();
    }


    protected Matrix4f initTransform()
    {
        return this.transform == null ? (this.transform = new Matrix4f()) : this.transform;
    }


    public void calculateTransform()
    {
        Matrix4f realInverted;
        Matrix4f real;
        if(this.owner.owner.animator.hasAnimations() && this.owner.owner.animator.currentAnimation != null)
        {
            Frame currentFrame = this.owner.owner.animator.currentAnimation.frames.get(owner.owner.animator.currentAnimation.currentFrame);
            realInverted = currentFrame.transforms.get(ID);
            real = currentFrame.invertTransforms.get(ID);
        }
        else
        {
         realInverted = this.restTransform;
         real = this.restInverted;
        }

        Matrix4f delta =  Matrix4f.mul(realInverted, real, null);
        transform = this.parent != null ? Matrix4f.mul(parent.transform, delta, initTransform()) : delta;
        Matrix4f absolute = Matrix4f.mul(real, transform, null);
        prevInverted = Matrix4f.invert(absolute, null);
        children.forEach(Bone::calculateTransform);
    }

    public void applyTransform()
    {
        Frame currentFrame = this.owner.owner.animator.currentAnimation.frames.get(owner.owner.animator.currentAnimation.currentFrame);

        if(currentFrame != null)
        {
            HashMap<Integer, Matrix4f> precalcArray = animatedTransforms.get(currentFrame.owner.name);
            Matrix4f animated = precalcArray.get(currentFrame.ID);
            Matrix4f animatedChange = new Matrix4f();
            Matrix4f.mul(animated, restInverted, animatedChange);
            transform = this.transform == null ? animatedChange : Matrix4f.mul(this.transform, animatedChange, this.transform);
        }

        verts.entrySet().forEach(transformVertexFloatEntry -> {
            transformVertexFloatEntry.getKey().calculateLocation(this, transformVertexFloatEntry.getValue());
        });

        owner.owner.sockets.entrySet().forEach(socketFloatEntry -> {
            if(socketFloatEntry.getKey().attachedBone == this)
            {
                socketFloatEntry.getKey().calculateLocation(this, socketFloatEntry.getValue());
            }
        });
        
        this.resetTransform();

    }

    /*
    This method will get a child from a string name
     */
    public Bone getChild(String name)
    {
        for(Bone bone : children)
        {
            if(bone.name == name) return bone;
        }

        ModelAPI.LOGGER.error("No child bone with the name " + name + " for bone - " + this.name + " ID " + this.ID);
        return null;
    }

    /*
    This method will get a child from the id.
     */
    public Bone getChild(int ID)
    {
        for(Bone bone : children)
        {
            if(bone.ID == ID) return bone;
        }

        ModelAPI.LOGGER.error("No child bone with the ID " + ID + " for bone - " + this.name + " ID " + this.ID);
        return null;
    }
}
