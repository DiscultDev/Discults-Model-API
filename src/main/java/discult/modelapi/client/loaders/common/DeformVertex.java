package discult.modelapi.client.loaders.common;

import org.joml.Matrix4f;
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

    public void applyTransform(Bone bone, float weight)
    {
        Matrix4f transform = bone.transformMatrix;

        if(transform != null)
        {
            if(currentLocation == null)
                currentLocation = new Vector4f();
            if(currentNormalLocation == null)
                currentNormalLocation = new Vector4f();

            Vector4f locTemp = transform.transform(restLocaton);
            Vector4f locNormTemp = transform.transform(restNormalLocation);

            locTemp.mul(weight);
            locNormTemp.mul(weight);

            currentLocation.add(locTemp);
            currentNormalLocation.add(locNormTemp);

        }
    }

    public void applyChange()
    {
        if (this.currentLocation == null) {
            this.x = this.restLocaton.x;
            this.y = this.restLocaton.y;
            this.z = this.restLocaton.z;
        } else {
            this.x = this.currentLocation.x;
            this.y = this.currentLocation.y;
            this.z = this.currentLocation.z;
        }

        if (this.currentNormalLocation == null) {
            this.xn = this.restNormalLocation.x;
            this.yn = this.restNormalLocation.y;
            this.zn = this.restNormalLocation.z;
        } else {
            this.xn = this.currentNormalLocation.x;
            this.yn = this.currentNormalLocation.y;
            this.zn = this.currentNormalLocation.z;
        }

    }


}
