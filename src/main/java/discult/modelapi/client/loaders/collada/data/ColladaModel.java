package discult.modelapi.client.loaders.collada.data;

public class ColladaModel
{
    private final ColladaSkeleton skeleton;
    private final ColladaMesh mesh;

    public ColladaModel(ColladaMesh mesh, ColladaSkeleton skeleton) {
        this.skeleton = skeleton;
        this.mesh = mesh;
    }

    public ColladaMesh getMeshData()
    {
        return mesh;
    }


}
