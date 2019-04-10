package discult.modelapi.client.loaders.collada.data;

import java.util.ArrayList;
import java.util.List;

public class ColladaKeyframe
{
    public final float time;
    public final List<ColladaBoneTransform> boneTransforms = new ArrayList<>();

    public ColladaKeyframe(float time){
        this.time = time;
    }

    public void addBoneTransform(ColladaBoneTransform transform){
        boneTransforms.add(transform);
    }
}
