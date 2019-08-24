package discult.modelapi.client.loaders.common;

import java.util.ArrayList;
import java.util.List;

public class Skeleton
{

    /*
    The model the skeleton belongs to.
     */
    public AnimatedModel model;

    private final List<Bone> BONES = new ArrayList<>();


    public Skeleton(AnimatedModel model)
    {
        this.model = model;
    }

    public List<Bone> getBones()
    {
        return BONES;
    }


    /**
     * @param ID This is the ID that identifies the bone.
     * @return the bone that the ID belongs to.
     */
    public Bone getBone(int ID)
    {
        for(Bone bone : BONES)
        {
            if(bone.getID() == ID)
            {
                return bone;
            }
        }

        return null;
    }


    /**
     * @param name This is the name that identifies the bone.
     * @return the bone that the name belongs to.
     */
    public Bone getBone(String name)
    {
        for(Bone bone : BONES)
        {
            if(bone.getName().equalsIgnoreCase(name))
            {
                return bone;
            }
        }

        return null;
    }

    /*
    TODO think of better name for method.
    this is to be run at the end of the loader once all the information has been processed from the model
    this will fix up the bones to ensure thier parents are correct.
     */
    public void fixUp()
    {
        //Fixes the bones to thier proper parent.
        BONES.forEach(bone ->
        {
            if(bone.getParentID() < 0) bone.setParent(null);
            else bone.setParent(getBone(bone.getParentID()));
        });

    }
}
