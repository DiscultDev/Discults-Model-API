package discult.modelapi.client.loaders.collada;

public class ColladaMesh
{
    private float[] vertices, texCoords, normals, vertexWeights;
    private int[] indices, boneIDs;

    public ColladaMesh(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
                       int[] boneIDs, float[] vertexWeights) {
        this.vertices = vertices;
        this.texCoords = textureCoords;
        this.normals = normals;
        this.vertexWeights = vertexWeights;
        this.indices = indices;
        this.boneIDs = boneIDs;
    }

    public int[] getBoneIDs()
    {
        return boneIDs;
    }

    public float[] getVertexWeights()
    {
        return vertexWeights;
    }

    public float[] getVertices()
    {
        return vertices;
    }

    public float[] getTexCoords()
    {
        return texCoords;
    }

    public float[] getNormals()
    {
        return normals;
    }

    public int[] getIndices()
    {
        return indices;
    }

    public int getVertexCount()
    {
        return vertices.length / 3;
    }






}
