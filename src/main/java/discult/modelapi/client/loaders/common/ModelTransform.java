package discult.modelapi.client.loaders.common;

import javax.vecmath.Matrix4f;

/**
 *Model Transform for a bone.
 */
public class ModelTransform {

    public void updateModelTransform(Transform localTransform, Bone parent)
    {

    }

    public void getOffsetTransform(Matrix4f outTransform, Matrix4f inverseModelBindMatrix)
    {

    }

    public void applyBindPose(Transform localTransform, Matrix4f inverseModelBindMatrix, Bone parent)
    {

    }

    public Transform getModelTransform()
    {
        return new Transform();
    }
}