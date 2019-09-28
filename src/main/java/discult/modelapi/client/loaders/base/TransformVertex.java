package discult.modelapi.client.loaders.base;

import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

/**
 * This is an extension from Vertex, but this stores information that
 * can be update and manipulated. 
 * This will also provide vertex-normal positions and store the Vector position
 * of the vertex.
 */
public class TransformVertex extends Vertex
{
	public final int ID;//The id for the Vertex
	public float xn,yn,zn;//the x,y,z normal positions of the vertex.
	public final Vector4f restPosition;//rest location for the vertex(the position read from the model file).
	public final Vector4f restNormalPosition;//rest Normal location for the vertex(the position read from the model file).
	public Vector4f realPosition = new Vector4f();//its final position calculated from a matrix transform applied to a rest position.
	public Vector4f realNormalPosition = new Vector4f();//its final normal position calculated from a matrix transform applied to a rest position.


	/**
	 * Base constructor for the class this asks for the bare minimum that you need to initiate a vertex.
	 */
	public TransformVertex(float x, float y, float z, float xn, float yn, float zn, int ID) {
		super(x, y, z);
		this.ID = ID;
		this.xn = xn;
		this.yn = yn;
		this.zn = zn;
		
		this.restPosition = new Vector4f(x,y,z,1);
		this.restNormalPosition = new Vector4f(xn,yn,zn,0);
	}
	
	/**
	 * This method is used to ensure the vectors don't return null.
	 */
	public void initPosition()
	{
		if (realNormalPosition == null) realNormalPosition = new Vector4f();
		
		if(realPosition == null) realPosition = new Vector4f();
	}
	
	
	/**
	 * Used to calculate the location of the vertex from the Matrix4f transform provided by the bone.
	 */
	public void calculateLocation(Bone bone, float weight)
	{
		Matrix4f transform = bone.transform;
		
		
		Vector4f tempPos = Matrix4f.transform(transform, restPosition, null);
		tempPos.scale(weight);
		Vector4f tempNormalPos = Matrix4f.transform(transform, restNormalPosition, null);
		tempNormalPos.scale(weight);
		
		Vector4f.add(tempPos, realPosition, realPosition);
		Vector4f.add(tempNormalPos, realNormalPosition, realNormalPosition);
	}
	
	/**
	 * This will be called when the user is actually ready to apply a change to the position of the vertex. 
	 */
	public void applyChange()
	{
		if (this.realPosition == null) {
	         this.x = this.restPosition.x;
	         this.y = this.restPosition.y;
	         this.z = this.restPosition.z;
	      } else {
	         this.x = this.realPosition.x;
	         this.y = this.realPosition.y;
	         this.z = this.realPosition.z;
	      }

	      if (this.realNormalPosition == null) {
	         this.xn = this.restNormalPosition.x;
	         this.yn = this.restNormalPosition.y;
	         this.zn = this.restNormalPosition.z;
	      } else {
	         this.xn = this.realNormalPosition.x;
	         this.yn = this.realNormalPosition.y;
	         this.zn = this.realNormalPosition.z;
	      }

	}
	
	
}
