package discult.modelapi.client.loaders.common;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Frame
{

    public List<Matrix4f> transforms = new ArrayList<>();
    public List<Matrix4f> invertTransforms = new ArrayList<>();

    public Animation owner;
    public final int ID;

    public Frame(Animation owner)
    {
        this.owner = owner;
        ID = owner.requestFrameID();
    }
}
