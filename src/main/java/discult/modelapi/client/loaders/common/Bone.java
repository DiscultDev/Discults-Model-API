package discult.modelapi.client.loaders.common;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is a class to represents a bone is a skeleton structure.
 *
 * { int ID}
 */
public class Bone
{
    private final int ID;
    private final String NAME;
    /*
    Some model loaders don't register the bones in order so this is to ensure that most model formats will load and
    work without needing to modify.
     */
    private int parentID;

    /*
    This will be loaded at the end of the models initialisation through the Skeleton class using the fixUp() method
    */
    private Bone parent;


    /*
     * at the end this will also recalculate to ensure the rest transform is correct to world space and not bone space.
     */
    public Matrix4f restMatrix,
    inverseRestMatrix,
    transformMatrix;

    /*
    This is the skeleton structure the bone is apart of.
     */
    public Skeleton skeleton;

    /*
    This is a list of all children associated with this bone.
     */
    public List<Bone> children = new ArrayList<>();


    /*
    This is a map of all the vertices that this bone can manipulate and the weight value it can manipulate it by.
     */
    public Map<DeformVertex, Float> verts = new HashMap<>();

    /*
    This is a map that stores
     */
    public Map<String, Map<Integer, Matrix4f>> animationTransforms = new HashMap<>();




    public int getID()
    {
        return ID;
    }

    public String getName()
    {
        return NAME;
    }

    public Bone(int ID, String name, int parentID)
    {
        this.ID = ID;
        this.NAME = name;
        this.parentID = parentID;


    }

    public int getParentID()
    {
        return parentID;
    }

    public Bone getParent()
    {
        return parent;
    }

    public void setParent(Bone parent)
    {
        this.parent = parent;
    }


    public void inverseRestMatrix()
    {
        this.inverseRestMatrix = restMatrix;
        inverseRestMatrix.invert();
    }

    public void reset()
    {
        this.transformMatrix.identity();
    }

    /**
     * This method will convert/reform the matrix from (bone space) to (world space)
     * @param parentMatrix
     */
    public void reform(Matrix4f parentMatrix)
    {
        parentMatrix.mul(restMatrix, restMatrix);
        //TODO put debug method
        this.reformChildren();
    }

    public void reformChildren()
    {
        children.forEach(child -> child.reform(this.restMatrix));
    }

    /**
     * This will init the transform matrix if it is null when it needs to be called.
     * @return the transform matrix;
     */
    protected Matrix4f initTransform()
    {
        return this.transformMatrix == null ? this.transformMatrix = new Matrix4f() : this.transformMatrix;
    }


    public void precalcAnimation()
    {
        
    }

    /**
     * This will set the TransformMatrix
     */
    public void setTransformMatrix()
    {
        Matrix4f realInverted;
        Matrix4f real;

        if(this.skeleton.model.animator.hasAnimations() && this.skeleton.model.animator.currentAnimation != null)
        {
            Frame currentFrame = this.skeleton.model.animator.currentAnimation.frames.get(this.skeleton.model.animator.currentAnimation.currentFrame);
            realInverted = currentFrame.transforms.get(this.ID);
            real = currentFrame.invertTransforms.get(this.ID);
        }
        else
        {
            realInverted = this.restMatrix;
            real = this.inverseRestMatrix;
        }

        Matrix4f delta = new Matrix4f();
        Matrix4f absolute = new Matrix4f();

        realInverted.mul(real, delta);

        this.transformMatrix = this.parent != null ? this.parent.transformMatrix.mul(delta, this.initTransform()) : delta;

        real.mul(this.transformMatrix = this.parent != null ? this.parent.transformMatrix.mul(delta, this.initTransform()) : delta, absolute);

        //TODO invert absolute to prevInverted if

        this.children.forEach(Bone::setTransformMatrix);
    }

}
