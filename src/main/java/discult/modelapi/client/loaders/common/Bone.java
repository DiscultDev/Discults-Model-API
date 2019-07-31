package discult.modelapi.client.loaders.common;

import discult.modelapi.utils.math.Matrix4;
import discult.modelapi.utils.math.Vec4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bone
{
    private final int ID;
    private final String name;
    private final Bone parent;

    private Vec4 position;
    private Matrix4 bindMatrix = new Matrix4();
    private Matrix4 transformMatrix;
    private Matrix4 inverseBindMatrix = new Matrix4();

    //TODO check if this can actually be final.
    public final List<Bone> children = new ArrayList<>();
    public final Map<String, Map<Integer, Matrix4>> animations = new HashMap<>();
    public final Map<Vertex, Float> verts = new HashMap<>();


    public Bone(int ID, String name, Bone parent)
    {
        this.ID = ID;
        this.name = name;
        this.parent = parent;
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

    public Vec4 getPosition()
    {
        return position;
    }

    public Matrix4 getTransformMatrix()
    {
        return transformMatrix;
    }

    public void setBindMatrix(Matrix4 bindMatrix)
    {
        this.bindMatrix = bindMatrix;
        setInverseBindMatrix(bindMatrix);
    }

    private void setInverseBindMatrix(Matrix4 bindMatrix)
    {
        this.inverseBindMatrix = Matrix4.invert(bindMatrix, this.inverseBindMatrix);
    }

    private void calculateModified()
    {

    }
}
