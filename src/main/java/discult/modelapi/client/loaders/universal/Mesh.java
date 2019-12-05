package discult.modelapi.client.loaders.universal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Mesh
{
    public String name;
    public List<Face> faces = new ArrayList<>();
    public String Skeleton;

    public Mesh(String name) {
        this.name = name;
    }
}
