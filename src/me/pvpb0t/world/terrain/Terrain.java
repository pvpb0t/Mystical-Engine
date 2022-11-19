package me.pvpb0t.world.terrain;

import me.pvpb0t.render.Loader;
import me.pvpb0t.render.model.TexturedModel;
import me.pvpb0t.render.model.raw.Texture;
import me.pvpb0t.render.model.raw.VaoObject;
import me.pvpb0t.util.Logger;
import me.pvpb0t.util.math.MathUtil;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    private static final float SIZE = 1000;
    private static final float MAX_HEIGHT=80;
    private static final float MAX_PIXEL_COLOUR=256*256*256;

    private float x, z;
    private VaoObject vaoObject;

    private float[][] heightTable;


    public TerrainTextureContainer getTextureContainer() {
        return textureContainer;
    }

    public void setTextureContainer(TerrainTextureContainer textureContainer) {
        this.textureContainer = textureContainer;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public void setBlendMap(TerrainTexture blendMap) {
        this.blendMap = blendMap;
    }

    private TerrainTextureContainer textureContainer;
    private TerrainTexture blendMap;


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public VaoObject getVaoObject() {
        return vaoObject;
    }

    public void setVaoObject(VaoObject vaoObject) {
        this.vaoObject = vaoObject;
    }


    public Terrain(int x, int z, Loader loader, TerrainTextureContainer textureContainer, TerrainTexture blendMap, String heightmap){
        this.textureContainer = textureContainer;
        this.blendMap = blendMap;
        this.x = x*SIZE;
        this.z = z*SIZE;
        vaoObject = this.generateTerrain(loader, heightmap);
    }




    public float[][] getHeightTable() {
        return heightTable;
    }

    //https://youtu.be/6E2zjfzMs7c - 2022-10-20
    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / (float) (heightTable.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heightTable.length - 1 || gridZ >= heightTable.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;
        if (xCoord <= (1 - zCoord)) {
            answer = MathUtil.barryCentric(new Vector3f(0, heightTable[gridX][gridZ], 0), new Vector3f(1, heightTable[gridX + 1][gridZ], 0), new Vector3f(0, heightTable[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            answer = MathUtil.barryCentric(new Vector3f(1, heightTable[gridX + 1][gridZ], 0), new Vector3f(1, heightTable[gridX + 1][gridZ + 1], 1), new Vector3f(0, heightTable[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return answer;
    }


    //https://youtu.be/yNYwZMmgTJk?t=181 2022-09-23
    private VaoObject generateTerrain(Loader loader, String heightmap){

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/texture/"+heightmap+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int VERTEX_COUNT=image.getHeight();
        heightTable = new float[VERTEX_COUNT][VERTEX_COUNT];
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height=getHeight(j,i,image);
                vertices[vertexPointer*3+1] = height;
                heightTable[j][i] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j,i,image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.injectToVAO(vertices, indices, normals, textureCoords);
    }

    private float getHeight(int x, int z, BufferedImage image){
        if(x<0||x>=image.getWidth()||z<0||z>=image.getWidth()){
            return 0;
        }else {
            float height = image.getRGB(x,z);
            height+=MAX_PIXEL_COLOUR;
            height/=MAX_PIXEL_COLOUR;
            height*=MAX_HEIGHT;
            return height;
        }

    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image){
        float heightL = getHeight(x-1, z,image);
        float heightR = getHeight(x+1, z, image);
        float heightD = getHeight(x, z-1,image);
        float heightU = getHeight(x, z+1, image);

        Vector3f normal = new Vector3f(heightL-heightR,1f,heightD-heightU);
        normal.normalise();
        return normal;
    }

}
