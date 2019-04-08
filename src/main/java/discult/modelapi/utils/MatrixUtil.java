package discult.modelapi.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MatrixUtil
{

    /*
    simple vectors that set what axis the matrix will rotate around.
     */
    static final Vector3f
            X_AXIS = new Vector3f(1,0,0),
            Y_AXIS = new Vector3f(0,1,0),
            Z_AXIS = new Vector3f(0,0,1);

    /**
     *Creates a matrix from float values.
     * TODO - if i use a model format that allows scaling in animations add the scale params.
     */
    public static Matrix4f createMatrix4FromTransLoc(float x, float y, float z, float xr, float yr, float zr)
    {
        Vector3f location = new Vector3f(x,y,z);
        Matrix4f mat4 = new Matrix4f();

        mat4.translate(location);
        mat4.rotate(xr, X_AXIS);
        mat4.rotate(yr, Y_AXIS);
        mat4.rotate(zr, Z_AXIS);
        return mat4;
    }
}
