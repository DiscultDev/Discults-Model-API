package discult.modelapi.utils.math;


/**
 * This is a simple matrix math lib for Discult's model api.
 * This is to keep consistency for later version minecraft as they could change math lib at anytime.
 */
public class Matrix4
{
    public float
    m00,m01,m02,m03,
    m10,m11,m12,m13,
    m20,m21,m22,m23,
    m30,m31,m32,m33;

    /**
     * Constructor that will set the identity of the matrix when returning new matrix.
     */
    public Matrix4()
    {
        setIdentity();
    }


    public static Matrix4 mul(Matrix4 firstMatrix, Matrix4 secondMatrix, Matrix4 outMatrix) {
        if (outMatrix == null)
            outMatrix = new Matrix4();

        float m00 = firstMatrix.m00 * secondMatrix.m00 + firstMatrix.m10 * secondMatrix.m01 + firstMatrix.m20 * secondMatrix.m02 + firstMatrix.m30 * secondMatrix.m03;
        float m01 = firstMatrix.m01 * secondMatrix.m00 + firstMatrix.m11 * secondMatrix.m01 + firstMatrix.m21 * secondMatrix.m02 + firstMatrix.m31 * secondMatrix.m03;
        float m02 = firstMatrix.m02 * secondMatrix.m00 + firstMatrix.m12 * secondMatrix.m01 + firstMatrix.m22 * secondMatrix.m02 + firstMatrix.m32 * secondMatrix.m03;
        float m03 = firstMatrix.m03 * secondMatrix.m00 + firstMatrix.m13 * secondMatrix.m01 + firstMatrix.m23 * secondMatrix.m02 + firstMatrix.m33 * secondMatrix.m03;
        float m10 = firstMatrix.m00 * secondMatrix.m10 + firstMatrix.m10 * secondMatrix.m11 + firstMatrix.m20 * secondMatrix.m12 + firstMatrix.m30 * secondMatrix.m13;
        float m11 = firstMatrix.m01 * secondMatrix.m10 + firstMatrix.m11 * secondMatrix.m11 + firstMatrix.m21 * secondMatrix.m12 + firstMatrix.m31 * secondMatrix.m13;
        float m12 = firstMatrix.m02 * secondMatrix.m10 + firstMatrix.m12 * secondMatrix.m11 + firstMatrix.m22 * secondMatrix.m12 + firstMatrix.m32 * secondMatrix.m13;
        float m13 = firstMatrix.m03 * secondMatrix.m10 + firstMatrix.m13 * secondMatrix.m11 + firstMatrix.m23 * secondMatrix.m12 + firstMatrix.m33 * secondMatrix.m13;
        float m20 = firstMatrix.m00 * secondMatrix.m20 + firstMatrix.m10 * secondMatrix.m21 + firstMatrix.m20 * secondMatrix.m22 + firstMatrix.m30 * secondMatrix.m23;
        float m21 = firstMatrix.m01 * secondMatrix.m20 + firstMatrix.m11 * secondMatrix.m21 + firstMatrix.m21 * secondMatrix.m22 + firstMatrix.m31 * secondMatrix.m23;
        float m22 = firstMatrix.m02 * secondMatrix.m20 + firstMatrix.m12 * secondMatrix.m21 + firstMatrix.m22 * secondMatrix.m22 + firstMatrix.m32 * secondMatrix.m23;
        float m23 = firstMatrix.m03 * secondMatrix.m20 + firstMatrix.m13 * secondMatrix.m21 + firstMatrix.m23 * secondMatrix.m22 + firstMatrix.m33 * secondMatrix.m23;
        float m30 = firstMatrix.m00 * secondMatrix.m30 + firstMatrix.m10 * secondMatrix.m31 + firstMatrix.m20 * secondMatrix.m32 + firstMatrix.m30 * secondMatrix.m33;
        float m31 = firstMatrix.m01 * secondMatrix.m30 + firstMatrix.m11 * secondMatrix.m31 + firstMatrix.m21 * secondMatrix.m32 + firstMatrix.m31 * secondMatrix.m33;
        float m32 = firstMatrix.m02 * secondMatrix.m30 + firstMatrix.m12 * secondMatrix.m31 + firstMatrix.m22 * secondMatrix.m32 + firstMatrix.m32 * secondMatrix.m33;
        float m33 = firstMatrix.m03 * secondMatrix.m30 + firstMatrix.m13 * secondMatrix.m31 + firstMatrix.m23 * secondMatrix.m32 + firstMatrix.m33 * secondMatrix.m33;

        outMatrix.m00 = m00;
        outMatrix.m01 = m01;
        outMatrix.m02 = m02;
        outMatrix.m03 = m03;
        outMatrix.m10 = m10;
        outMatrix.m11 = m11;
        outMatrix.m12 = m12;
        outMatrix.m13 = m13;
        outMatrix.m20 = m20;
        outMatrix.m21 = m21;
        outMatrix.m22 = m22;
        outMatrix.m23 = m23;
        outMatrix.m30 = m30;
        outMatrix.m31 = m31;
        outMatrix.m32 = m32;
        outMatrix.m33 = m33;

        return outMatrix;
    }

    public void mul(Matrix4 firstMatrix, Matrix4 secondMatrix)
    {
        mul(firstMatrix, secondMatrix, this);
    }


    /**
     * Method that will set the identity to the original state.
     */
    public void setIdentity()
    {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }

    public void invert(Matrix4 matrixIn)
    {
        invert(matrixIn, this);
    }

    public static Matrix4 invert(Matrix4 matrixIn, Matrix4 out)
    {
        float determinant = matrixIn.determinant();

        if(determinant != 0)
        {

            if(out == null)
                out = new Matrix4();
            float dInv = 1f/determinant;

            float t00 =  determinant3x3(matrixIn.m11, matrixIn.m12, matrixIn.m13, matrixIn.m21, matrixIn.m22, matrixIn.m23, matrixIn.m31, matrixIn.m32, matrixIn.m33);
            float t01 = -determinant3x3(matrixIn.m10, matrixIn.m12, matrixIn.m13, matrixIn.m20, matrixIn.m22, matrixIn.m23, matrixIn.m30, matrixIn.m32, matrixIn.m33);
            float t02 =  determinant3x3(matrixIn.m10, matrixIn.m11, matrixIn.m13, matrixIn.m20, matrixIn.m21, matrixIn.m23, matrixIn.m30, matrixIn.m31, matrixIn.m33);
            float t03 = -determinant3x3(matrixIn.m10, matrixIn.m11, matrixIn.m12, matrixIn.m20, matrixIn.m21, matrixIn.m22, matrixIn.m30, matrixIn.m31, matrixIn.m32);
            // second row
            float t10 = -determinant3x3(matrixIn.m01, matrixIn.m02, matrixIn.m03, matrixIn.m21, matrixIn.m22, matrixIn.m23, matrixIn.m31, matrixIn.m32, matrixIn.m33);
            float t11 =  determinant3x3(matrixIn.m00, matrixIn.m02, matrixIn.m03, matrixIn.m20, matrixIn.m22, matrixIn.m23, matrixIn.m30, matrixIn.m32, matrixIn.m33);
            float t12 = -determinant3x3(matrixIn.m00, matrixIn.m01, matrixIn.m03, matrixIn.m20, matrixIn.m21, matrixIn.m23, matrixIn.m30, matrixIn.m31, matrixIn.m33);
            float t13 =  determinant3x3(matrixIn.m00, matrixIn.m01, matrixIn.m02, matrixIn.m20, matrixIn.m21, matrixIn.m22, matrixIn.m30, matrixIn.m31, matrixIn.m32);
            // third row
            float t20 =  determinant3x3(matrixIn.m01, matrixIn.m02, matrixIn.m03, matrixIn.m11, matrixIn.m12, matrixIn.m13, matrixIn.m31, matrixIn.m32, matrixIn.m33);
            float t21 = -determinant3x3(matrixIn.m00, matrixIn.m02, matrixIn.m03, matrixIn.m10, matrixIn.m12, matrixIn.m13, matrixIn.m30, matrixIn.m32, matrixIn.m33);
            float t22 =  determinant3x3(matrixIn.m00, matrixIn.m01, matrixIn.m03, matrixIn.m10, matrixIn.m11, matrixIn.m13, matrixIn.m30, matrixIn.m31, matrixIn.m33);
            float t23 = -determinant3x3(matrixIn.m00, matrixIn.m01, matrixIn.m02, matrixIn.m10, matrixIn.m11, matrixIn.m12, matrixIn.m30, matrixIn.m31, matrixIn.m32);
            // fourth row
            float t30 = -determinant3x3(matrixIn.m01, matrixIn.m02, matrixIn.m03, matrixIn.m11, matrixIn.m12, matrixIn.m13, matrixIn.m21, matrixIn.m22, matrixIn.m23);
            float t31 =  determinant3x3(matrixIn.m00, matrixIn.m02, matrixIn.m03, matrixIn.m10, matrixIn.m12, matrixIn.m13, matrixIn.m20, matrixIn.m22, matrixIn.m23);
            float t32 = -determinant3x3(matrixIn.m00, matrixIn.m01, matrixIn.m03, matrixIn.m10, matrixIn.m11, matrixIn.m13, matrixIn.m20, matrixIn.m21, matrixIn.m23);
            float t33 =  determinant3x3(matrixIn.m00, matrixIn.m01, matrixIn.m02, matrixIn.m10, matrixIn.m11, matrixIn.m12, matrixIn.m20, matrixIn.m21, matrixIn.m22);

            out.m00 = t00*dInv;
            out.m11 = t11*dInv;
            out.m22 = t22*dInv;
            out.m33 = t33*dInv;
            out.m01 = t10*dInv;
            out.m10 = t01*dInv;
            out.m20 = t02*dInv;
            out.m02 = t20*dInv;
            out.m12 = t21*dInv;
            out.m21 = t12*dInv;
            out.m03 = t30*dInv;
            out.m30 = t03*dInv;
            out.m13 = t31*dInv;
            out.m31 = t13*dInv;
            out.m32 = t23*dInv;
            out.m23 = t32*dInv;
            return out;

        }
        else
            return null;
    }

    public float determinant() {
        float f =
                m00
                        * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32)
                        - m13 * m22 * m31
                        - m11 * m23 * m32
                        - m12 * m21 * m33);
        f -= m01
                * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32)
                - m13 * m22 * m30
                - m10 * m23 * m32
                - m12 * m20 * m33);
        f += m02
                * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31)
                - m13 * m21 * m30
                - m10 * m23 * m31
                - m11 * m20 * m33);
        f -= m03
                * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31)
                - m12 * m21 * m30
                - m10 * m22 * m31
                - m11 * m20 * m32);
        return f;
    }

    /**
     * same as org.lwjgl.util.vector.matrix4f.
     */
    private static float determinant3x3(float t00, float t01, float t02,
                                        float t10, float t11, float t12,
                                        float t20, float t21, float t22)
    {
        return   t00 * (t11 * t22 - t12 * t21)
                + t01 * (t12 * t20 - t10 * t22)
                + t02 * (t10 * t21 - t11 * t20);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(m00).append(' ').append(m10).append(' ').append(m20).append(' ').append(m30).append('\n');
        buf.append(m01).append(' ').append(m11).append(' ').append(m21).append(' ').append(m31).append('\n');
        buf.append(m02).append(' ').append(m12).append(' ').append(m22).append(' ').append(m32).append('\n');
        buf.append(m03).append(' ').append(m13).append(' ').append(m23).append(' ').append(m33).append('\n');
        return buf.toString();
    }
}
