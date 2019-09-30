package discult.modelapi.client.loaders.base;

import java.util.HashMap;

/**
 * The animator class will hold all the current animations and will be used
 * to decide what animation is needed for a specific task.
 * 
 * This also provides as a direct link for the animations to access information from the model
 * if needed.
 */
public class Animator
{
    public Model owner;
    public HashMap<String, Animation> animations = new HashMap<>();
    public Animation currentAnimation;

    /**
     *
     * @return
     */
    public boolean hasAnimations()
    {
        return animations.size() > 0 ? true : false;
    }
}
