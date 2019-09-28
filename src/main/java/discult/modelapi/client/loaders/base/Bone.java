package discult.modelapi.client.loaders.base;

import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
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




}
