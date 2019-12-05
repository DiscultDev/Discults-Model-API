package discult.modelapi.client.loaders.universal;

import org.joml.Matrix4f;

public class Bone
{
    public int index;
    public String name;
    public int parentID;
    public Bone parent;
    public float x, y ,z, rotateX, rotateY, rotateZ, scaleX, scaleY, scaleZ;
    public Matrix4f transform = new Matrix4f();
    public Matrix4f boneTransform = new Matrix4f();
    public Matrix4f invBoneTransform = new Matrix4f();
    public Matrix4f worldTransform = new Matrix4f();

    public Bone(int index, String name, int parentID) {
        this.index = index;
        this.name = name;
        this.parentID = parentID;
        scaleX = scaleY = scaleZ = 1.0F;
    }

    public Matrix4f getBoneTransform() {
        return boneTransform;
    }

    public void setBoneTransform(Matrix4f boneTransform) {
        this.boneTransform.set(boneTransform);
        invBoneTransform.set(boneTransform.invert());
    }

    public Matrix4f computeTransform()
    {
        transform.set(worldTransform);
        transform.translate(x, y, z);
        transform.scale(scaleX, scaleY, scaleZ);


        //TODO see if i can remove the check for 0
        if(this.rotateY != 0.0f)
            transform.rotateY(rotateY);

        if(this.rotateX != 0.0f)
            transform.rotateX(rotateX);

        if(this.rotateZ != 0.0f)
            transform.rotateZ(rotateZ);

        Matrix4f temp;
        //TODO can come in and recalculate differently
        if(parent != null)
            temp = new Matrix4f(parent.transform).mul(transform);

        temp = new Matrix4f().mul(transform);

        transform.set(temp);
        temp.mul(invBoneTransform);

        return temp;
    }

    public void reset()
    {
        this.x = this.y = this.z = 0.0F;
        this.rotateX = this.rotateY = this.rotateZ = 0.0F;
        this.scaleX = this.scaleY = this.scaleZ = 1.0F;
    }
}
