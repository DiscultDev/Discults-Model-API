package discult.modelapi.client.loaders.universal;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Skeleton
{
    public String name;

    public List<Bone> bones = new ArrayList<>();
    public Matrix4f[] transform;

    public void addBone(Bone bone)
    {
        bones.add(bone);
    }

    public void initialise()
    {
        for(Bone bone : bones)
        {
            if(bone.parent != null)
            {
                bone.parent = getBone(bone.parentID);
                bone.worldTransform.set(bone.parent.boneTransform).invert().mul(bone.boneTransform);
            }
            else
            {
                bone.worldTransform.set(bone.boneTransform);
            }
        }

        Collections.sort(bones, new Comparator<Bone>() {
            @Override
            public int compare(Bone o1, Bone o2) {
                return o1.index -o2.index;
            }
        });

        this.transform = new Matrix4f[bones.size()];
    }


    public void setupTransforms()
    {
        for(Bone bone : bones)
        {
            this.transform[bone.index] = bone.computeTransform();
        }
    }

    private Bone getBone(int ID)
    {
        for(Bone bone : bones)
        {
            if(bone.index == ID)
            {
                return bone;
            }
        }

        return null;
    }

}
