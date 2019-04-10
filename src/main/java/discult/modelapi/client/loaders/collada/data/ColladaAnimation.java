package discult.modelapi.client.loaders.collada;

public class ColladaAnimation
{
    public final float length;
    public final ColladaKeyframe[] frames;

    public ColladaAnimation(float lengthSeconds, ColladaKeyframe[] keyFrames)
    {
        this.length = lengthSeconds;
        this.frames = keyFrames;
    }
}
