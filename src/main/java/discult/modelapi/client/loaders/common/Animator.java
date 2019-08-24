package discult.modelapi.client.loaders.common;

import java.util.HashMap;
import java.util.Map;

public class Animator
{
    /*
    The model the animator belongs to.
     */
    public AnimatedModel model;

    public Map<String, Animation> animations = new HashMap<>();
    public Animation currentAnimation;

    public Animator(AnimatedModel model)
    {
        this.model = model;
    }


    public boolean hasAnimations()
    {
        return animations.size() > 0 ? true : false;
    }
}
