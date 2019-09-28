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
	public Matrix4f transform = new Matrix4f();
	public List<Bone> children = new ArrayList<>();

    public HashMap<TransformVertex, Float> verts = new HashMap<>();

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
