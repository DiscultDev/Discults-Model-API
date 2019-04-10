package discult.modelapi.client.loaders.collada.data;

import org.joml.Matrix4f;

public class ColladaBoneTransform
{
    public final String jointName;
    public final Matrix4f transform;

    public ColladaBoneTransform(String jointName, Matrix4f transform) {
        this.jointName = jointName;
        this.transform = transform;
    }
}
