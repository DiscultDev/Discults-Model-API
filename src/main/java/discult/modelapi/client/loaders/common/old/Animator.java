package discult.modelapi.client.loaders.common.old;

import java.util.ArrayList;
import java.util.List;

public class Animator
{

    private List<Animation> animations = new ArrayList<>();

    public Model owner;
    private Animation currentAnimation;

    public boolean hasAnimations()
    {
        return animations.size() > 0 ? true : false;
    }

    public Animation getCurrentAnimation()
    {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation)
    {
        this.currentAnimation = currentAnimation;
    }
}
