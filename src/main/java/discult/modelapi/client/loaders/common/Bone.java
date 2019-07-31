package discult.modelapi.client.loaders.common;

import org.lwjgl.util.vector.Matrix4f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a bone of a skeleton system for animated models.
 */
public class Bone
{
    private String name;
    private int id;
    private Bone parent;
    private List<Bone> children = new ArrayList<>();
    private Mesh targetMesh;

    //Local Space Transform of a bone.
    private Transform localTransform = new Transform();
    /*
    Initial local space of the bone usually pulled from the model and is the transform it will set if localTransform
    is null or empty.
     */
    private Transform restTransform = new Transform();

    //The model space transform of the bone.
    private ModelTransform modelTransform;

    //This matrix is what is used to affect the vertex position of a model by the joints model space.
    private Matrix4f inverseModelTransform = new Matrix4f();



    public Bone(){}
    /**
     * Creates a new bone taking its id, name and parent if there is no parent set it to null.
     * @param id the id of the bone.
     * @param name the name of the bone.
     * @param parent the parent of the bone.
     */
    public Bone(int id, String name, @Nullable Bone parent)
    {
        this.name = name;
        this.id = id;
        this.parent = parent;
    }

    /**
     * Will update the current bone transform and the children of this bone.
     */
    public final void update()
    {
        modelTransform.updateModelTransform(localTransform, parent);

        for(Bone child : children)
        {
            child.update();
        }

    }
}
