package discult.modelapi.client.loaders.base;
/**
 * This represents a single vertex within a model.
 * This Vertex will not changed once its values have been stored.
 * and will only be used for a static model such as a model exported to the .OBJ file format.
 */
public class Vertex 
{

	public float x, y, z;

	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	
}
