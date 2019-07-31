package discult.modelapi.client.loaders.common;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * This holds a translation(Vector3f), rotation(Quat4f) and scale(Vector3f) inside a single object.
 */
public class Transform
{
    private Vector3f translation;
    private Quat4f rotation;
    private Vector3f scale;

    /**
     * Sets the translation and rotation of the transform defaulting scale to 1.
     * @param translation translation to be added to the transform.
     * @param rotation rotation to be added to the transform.
     */
    public Transform(Vector3f translation, Quat4f rotation)
    {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = new Vector3f(1,1,1);
    }

    /**
     * Sets the translation, scale and rotation of the transform.
     * @param translation translation to be added to the transform.
     * @param rotation rotation to be added to the transform.
     * @param scale scale to be added to the transform.
     */
    public Transform(Vector3f translation, Quat4f rotation, Vector3f scale)
    {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }


    /**
     * Basic constructor sets all values to a default state.
     */
    public Transform()
    {
        this(new Vector3f(), new Quat4f());
    }

    /**
     * Gets the translation Vector3f from this transform.
     * @return the translation Vector3f of this transform.
     */
    public Vector3f getTranslation()
    {
        return translation;
    }

    /**
     * Sets the translation in the transform to the provided Vector3f.
     * @param translation the new translation Vector3f for the Transform.
     */
    public void setTranslation(Vector3f translation)
    {
        this.translation = translation;
    }


    /**
     * Gets the rotation Quaternion(Quat4f) from the Transform.
     * @return the Quaternion(Quat4f) from the Transform.
     */
    public Quat4f getRotation()
    {
        return rotation;
    }

    /**
     * Sets the rotation in the transform to the provided Quaternion(Quat4f).
     * @param rotation - the new Quaternion(Quat4f) for the transform.
     */
    public void setRotation(Quat4f rotation)
    {
        this.rotation = rotation;
    }

    /**
     * Gets the scale Vector3f from the Transform.
     * @return the Vector3f from the Transform.
     */
    public Vector3f getScale()
    {
        return scale;
    }

    /**
     * Sets the scale Vector3f of the transform to the provided Vector3f.
     * @param scale - the new Vector3f for the transform.
     */
    public void setScale(Vector3f scale)
    {
        this.scale = scale;
    }



}
