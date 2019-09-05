package discult.modelapi.client.loaders.common;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animation
{
    public String animationName;
    public Animator animationController;

    public List<Frame> frames = new ArrayList<>();
    public Map<Integer, Matrix4f> animationTransforms = new HashMap<>();

    public int currentFrame = 0, lastFrame, totalFrames, frameIDBank;


    public Animation(String animationName, Animator animationController)
    {
        this.animationName = animationName;
        this.animationController = animationController;
    }

    public int requestFrameID() {
        final int result = this.frameIDBank;
        ++this.frameIDBank;
        return result;
    }

}
