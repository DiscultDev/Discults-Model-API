package discult.modelapi.client.loaders.common;

import org.joml.Vector4f;

public class DeformVertex extends Vertex
{
    public Vector4f currentLocation, currentNormalLocation;

    private final Vector4f restLocaton, restNormalLocation;

    public final int ID;

    public float xn, yn, zn;

    public DeformVertex(float x, float y, float z, float xn, float yn, float zn, int ID)
    {
        super(x, y, z);
        this.ID = ID;
        this.xn = xn;
        this.yn = yn;
        this.zn = zn;

        this.currentLocation = new Vector4f();
        this.currentNormalLocation = new Vector4f();
        this.restLocaton = new Vector4f(x, y, z, 1F);
        this.restNormalLocation = new Vector4f(xn, yn, zn,0F);
    }


}
