package discult.modelapi.client.loaders.collada;

public class ColladaSkeleton
{
    public final int jointCount;
    public final ColladaBone root;

    public ColladaSkeleton(int jointCount, ColladaBone root) {
        this.jointCount = jointCount;
        this.root = root;
    }
}
