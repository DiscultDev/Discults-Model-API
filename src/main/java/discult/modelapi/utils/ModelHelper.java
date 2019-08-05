package discult.modelapi.utils;


import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class ModelHelper
{
    public static Matrix4f createMatrix(Vector3f pos, Quat4f rot, Vector3f scale)
    {
        Matrix4f mat = new Matrix4f();

        mat.setTranslation(pos);
        mat.setRotation(rot);

        mat = scaleMatrix(mat, scale);

       return mat;

    }

    public static Matrix4f scaleMatrix(Matrix4f mat, Vector3f scale)
    {
        mat.m00 = mat.m00 * scale.x;
        mat.m01 = mat.m01 * scale.x;
        mat.m02 = mat.m02 * scale.x;
        mat.m03 = mat.m03 * scale.x;
        mat.m10 = mat.m10 * scale.y;
        mat.m11 = mat.m11 * scale.y;
        mat.m12 = mat.m12 * scale.y;
        mat.m13 = mat.m13 * scale.y;
        mat.m20 = mat.m20 * scale.z;
        mat.m21 = mat.m21 * scale.z;
        mat.m22 = mat.m22 * scale.z;
        mat.m23 = mat.m23 * scale.z;

        return mat;
    }
}
