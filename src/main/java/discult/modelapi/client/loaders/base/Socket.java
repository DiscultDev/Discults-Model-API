package discult.modelapi.client.loaders.base;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Socket will be used to attach different models e.g. item models to a certain bones
 * location and follow with its movements. Sockets will act as a vert in the sense it will pull information from
 * the bone and manipulate it to follow along with the bones movements. 
 */
public class Socket 
{
	public final String name;
	public float x,y,z;
    public final Vector4f restPosition;//rest location for the Socket
    public Vector4f realPosition = new Vector4f();//its final position calculated from a matrix transform applied to a rest position.


    /**
     * Base constructor for the class this asks for the bare minimum that you need to initiate a vertex.
     */
    public Socket(String name, float x, float y, float z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;

        this.restPosition = new Vector4f(x,y,z,1);
    }

    /**
     * This method is used to ensure the vector doesn't return null.
     */
    public void initPosition()
    {
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

        Vector4f.add(tempPos, realPosition, realPosition);
    }

    /**
     * This will be called when the user is actually ready to apply the change to the position of the socket.
     */
    public void applyChange()
    {
        if(realPosition == null)
        {
            x = restPosition.x;
            y = restPosition.y;
            z = restPosition.z;
        }
        else
        {
            x = realPosition.x;
            y = realPosition.y;
            z = realPosition.z;
        }


    }
}
