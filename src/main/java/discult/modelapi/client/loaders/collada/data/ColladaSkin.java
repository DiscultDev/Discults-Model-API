package discult.modelapi.client.loaders.collada;

import java.util.List;

public class ColladaSkin
{
    public final List<String> jointOrder;
    public final List<ColladaVertexSkinInfo> verticesSkinInfo;

    public ColladaSkin(List<String> jointOrder, List<ColladaVertexSkinInfo> verticesSkinInfo){
        this.jointOrder = jointOrder;
        this.verticesSkinInfo = verticesSkinInfo;
    }
}
