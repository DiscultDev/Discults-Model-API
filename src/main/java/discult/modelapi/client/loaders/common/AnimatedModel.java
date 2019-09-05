package discult.modelapi.client.loaders.common;

public class AnimatedModel
{
    /*
    The Skeleton the model is the parent of.
     */
    public Skeleton skeleton;

    /*
    The Animator the model is the parent of.
     */
    public Animator animator;


    /**
     * This will create the model and produce a new skeleton and animator for you.
     */
    public AnimatedModel()
    {
        this.skeleton = new Skeleton(this);
        this.animator = new Animator(this);
    }
    /**
     * This will create the model.
     * @param skeleton the skeleton the model is the parent of (usually do a new Skeleton(this)).
     * @param animator the animator the model is the parent of (usually do a new Animator(this)).
     */
    public AnimatedModel(Skeleton skeleton, Animator animator)
    {
        this.skeleton = skeleton;
        this.animator = animator;
    }

    public Frame currentFrame() {
        return this.animator.currentAnimation == null ? null : (this.animator.currentAnimation.frames == null ? null : (this.animator.currentAnimation.frames.isEmpty() ? null : this.animator.currentAnimation.frames.get(this.animator.currentAnimation.currentFrame)));
    }

}
