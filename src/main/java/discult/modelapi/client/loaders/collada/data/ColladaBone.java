package discult.modelapi.client.loaders.collada;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ColladaBone
{
    public final int ID;
    public final String nameID;
    public final Matrix4f bindLocalTransform;

    public final List<ColladaBone> children = new ArrayList<>();

    public ColladaBone(int ID, String nameID, Matrix4f bindLocalTransform) {
        this.ID = ID;
        this.nameID = nameID;
        this.bindLocalTransform = bindLocalTransform;
    }

    public void addChild(ColladaBone child) {
        children.add(child);
    }
}
