package discult.modelapi.client.loaders.common.old;

import discult.modelapi.utils.ModelHelper;
import discult.modelapi.utils.math.Matrix4;
import discult.modelapi.utils.math.Quat;
import discult.modelapi.utils.math.Vec4;

import java.util.ArrayList;
import java.util.List;

public class Frame
{
    public Animation owner;
    public final int frameID;
    public List<Matrix4> transform = new ArrayList<>();
    public List<Matrix4> invertTransform = new ArrayList<>();

    public Frame(Animation owner, int frameID)
    {
        this.owner = owner;
        this.frameID = frameID;
    }

    public void addTransforms(int frameID, Matrix4 invertedData)
    {
        this.transform.add(frameID, invertedData);
        this.invertTransform.add( frameID, Matrix4.invert(invertedData, null));

    }

    public void fixUp(int id, float degrees)
    {
        float radians = (float)Math.toRadians(degrees);

        Matrix4 rotateFactor = ModelHelper.createMatrix(new Vec4(0,0,0,1), new Quat(radians, 0,0,1) , new Vec4(1,1,1,1));
        Matrix4.mul(rotateFactor, this.transform.get(id), this.transform.get(id));
        Matrix4.mul(Matrix4.invert(rotateFactor, null), this.invertTransform.get(id), this.invertTransform.get(id));
    }

    /**
     * Will reform the matrix based on the parent bone
     */
    public void reform() {
        for(int i = 0; i < this.transform.size(); ++i) {
            Bone bone = this.owner.owner.owner.getSkeleton().getBoneList().get(i);
            
            if (bone.getParent() != null) {
                Matrix4 temp = Matrix4.mul(this.transform.get(bone.getParent().getID()), this.transform.get(i), null);
                this.transform.set(i, temp);
                this.invertTransform.set(i, Matrix4.invert(temp, null));
            }
        }

    }

}
