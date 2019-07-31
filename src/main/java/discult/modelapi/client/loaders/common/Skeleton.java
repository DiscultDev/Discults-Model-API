package discult.modelapi.client.loaders.common;

import discult.modelapi.DiscultsModelAPI;

import java.util.ArrayList;
import java.util.List;

public class Skeleton
{
    private List<Bone> skeleton = new ArrayList<>();


    public Bone getBone(String name)
    {
        return skeleton.stream()
                .filter(bone -> bone.getName().toLowerCase() == name.toLowerCase())
                .findFirst().orElse(null);

    }

    public Bone getBone(int ID)
    {
        return skeleton.stream()
                .filter(bone -> bone.getID() == ID)
                .findFirst().orElse(null);
    }

    public void addBone(Bone bone)
    {
        //This will simple warn the user that some bones are duplicates.
        for(Bone bones : skeleton)
        {
            if(bones.getID() == bone.getID())
            {
                DiscultsModelAPI.LOGGER.warn("Two or more bones have the same ID this can cause problems later.");
            }
            else if(bones.getName().toLowerCase() == bone.getName().toLowerCase())
            {
                DiscultsModelAPI.LOGGER.warn("Two or more bones have the same NAME this can cause problems later.");
            }
        }
        skeleton.add(bone);
    }
}
