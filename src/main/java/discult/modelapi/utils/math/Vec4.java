package discult.modelapi.utils.math;

public class Vec4
{

    public float x,y,z,w;

    public Vec4() {}

    public Vec4(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vec4 transform(Matrix4 matrix, Vec4 vec4, Vec4 out)
    {
        if(out == null)
            out = new Vec4();

       out.x = matrix.m00 * vec4.x + matrix.m10 * vec4.y + matrix.m20 * vec4.z + matrix.m30 * vec4.w;
       out.y = matrix.m01 * vec4.x + matrix.m11 * vec4.y + matrix.m21 * vec4.z + matrix.m31 * vec4.w;
       out.z = matrix.m02 * vec4.x + matrix.m12 * vec4.y + matrix.m22 * vec4.z + matrix.m32 * vec4.w;
       out.w = matrix.m03 * vec4.x + matrix.m13 * vec4.y + matrix.m23 * vec4.z + matrix.m33 * vec4.w;

       return out;
    }

    public void transform(Matrix4 matrix, Vec4 vec4)
    {
        transform(matrix, vec4, this);
    }
}
