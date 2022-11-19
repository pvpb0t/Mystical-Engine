package me.pvpb0t.render.model.raw;

public class VaoObject {

    private int vaoID;
    private int vertexCount;

    public VaoObject(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public void setVaoID(int vaoID) {
        this.vaoID = vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }
}
