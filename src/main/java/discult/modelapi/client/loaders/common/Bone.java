package discult.modelapi.client.loaders.common;

import discult.modelapi.utils.ModelHelper;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bone
{
    private final int ID;
    private final String name;
    private final Bone parent;


    private Vector3f bindPos = new Vector3f();
    private Quat4f bindRot = new Quat4f();
    private Vector3f bindScale = new Vector3f(1,1,1);

    private final Matrix4f bindMatrix = new Matrix4f();
    private final Matrix4f transformMatrix = new Matrix4f();
    private final Matrix4f inverseBindMatrix = new Matrix4f();

    private final List<Bone> children = new ArrayList<>();
    private final Map<Integer, Float> verts = new HashMap<>();


    public Bone(int ID, String name, Bone parent)
    {

        if(ID < 0)
        {
            throw new IllegalArgumentException("Bone id cannot be less than 0");
        }

        this.ID = ID;

        if(name == null)
        {
            throw new IllegalArgumentException("Bone name cannot be null");
        }

        this.name = name;

        this.parent = parent;

        bindMatrix.setIdentity();
        transformMatrix.setIdentity();
        inverseBindMatrix.setIdentity();
    }


    public void addVertexWeight(int vertID, float weight)
    {
        this.verts.put(vertID, weight);
    }

    protected void calculateBindMatrices()
    {
       this.bindMatrix.set(ModelHelper.createMatrix(bindPos, bindRot, bindScale));

       if(this.parent == null) transformMatrix.set(bindMatrix);

       else this.parent.transformMatrix.mul(bindMatrix, transformMatrix);

       this.transformMatrix.invert(inverseBindMatrix);
    }

    public int getID()
    {
        return ID;
    }

    public String getName()
    {
        return name;
    }

    public Bone getParent()
    {
        return parent;
    }

    public Vector3f getBindPos()
    {
        return bindPos;
    }

    public Quat4f getBindRot()
    {
        return bindRot;
    }

    public Vector3f getBindScale()
    {
        return bindScale;
    }

    public Matrix4f getBindMatrix()
    {
        return bindMatrix;
    }

    public Matrix4f getTransformMatrix()
    {
        return transformMatrix;
    }

    public Matrix4f getInverseBindMatrix()
    {
        return inverseBindMatrix;
    }

    public List<Bone> getChildren()
    {
        return children;
    }

    public Map<Integer, Float> getVerts()
    {
        return verts;
    }
}
