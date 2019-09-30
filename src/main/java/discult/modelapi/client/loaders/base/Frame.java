package discult.modelapi.client.loaders.base;

import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;

/**
 * This class handles a single frame within the animated model.
 * A frame will hold position,rotation and possible scale that a bone must be
 * transformed to during the frame.
 */
public class Frame 
{
    public Animation owner;
    public int ID;
    public ArrayList<Matrix4f> transforms = new ArrayList<>();
    public ArrayList<Matrix4f> invertTransforms = new ArrayList();
}
