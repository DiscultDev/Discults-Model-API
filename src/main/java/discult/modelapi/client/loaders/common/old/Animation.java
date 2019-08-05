package discult.modelapi.client.loaders.common.old;

import java.util.ArrayList;
import java.util.List;

public class Animation implements ISkeletonOwner
{

    public Animator owner;
    public List<Frame> frames = new ArrayList();
    public int currentFrameIndex = 0;

}
