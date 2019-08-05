package discult.modelapi.client.loaders.common.old;

import discult.modelapi.utils.math.Matrix4;
import discult.modelapi.utils.math.Quat;
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
    private final Skeleton owner;

    private Vec4 position = new Vec4(0,0,0,1);
    private Quat rotation = new Quat(0,0,0,1);
    private Vec4 scale = new Vec4(1,1,1,1);
    private Matrix4 bindMatrix = new Matrix4();
    private Matrix4 transformMatrix = new Matrix4();
    private Matrix4 inverseBindMatrix = new Matrix4();

    //TODO check if this can actually be final.
    public final List<Bone> children = new ArrayList<>();
    public final Map<Vertex, Float> verts = new HashMap<>();


    public Bone(int ID, String name, Bone parent, Skeleton owner)
    {
        this.ID = ID;
        this.name = name;
        this.parent = parent;
        this.owner = owner;
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

    public Skeleton getOwner()
    {
        return owner;
    }

    public Vec4 getPosition()
    {
        return position;
    }

    public Quat getRotation()
    {
        return rotation;
    }

    public Vec4 getScale()
    {
        return scale;
    }

    public Matrix4 getBindMatrix()
    {
        return bindMatrix;
    }

    public Matrix4 getTransformMatrix()
    {
        return transformMatrix;
    }

    public void setTransformMatrix(Matrix4 transformMatrix)
    {
        this.transformMatrix = transformMatrix;
    }

    public Matrix4 getInverseBindMatrix()
    {
        return inverseBindMatrix;
    }
}
