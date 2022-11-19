package me.pvpb0t.render;

import me.pvpb0t.render.model.raw.VaoObject;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static me.pvpb0t.util.BufferUtil.dataToFloatBuffer;
import static me.pvpb0t.util.BufferUtil.dataToIntBuffer;

public class Loader {

    private ArrayList<Integer> vaos = new ArrayList<>();
    private ArrayList<Integer> vbos = new ArrayList<>();
    private ArrayList<Integer> textures = new ArrayList<>();

    public VaoObject injectToVAO(float[] verticies, int[] indicies, float[] normals,float[] texturepos){
        int vaoID = createVAO();
        bindIndicesBuffer(indicies);
        //Injekterar data in i gpu attributelist
        dataToAttributeList(0,3, verticies);
        dataToAttributeList(1,2, texturepos);
        dataToAttributeList(2,3, normals);
        unbindVAO();
        return new VaoObject(vaoID, indicies.length);
    }
    public int injectToVAO(float[] positions, float[] textureCoords) {
        int vaoID = createVAO();
        dataToAttributeList(0, 2, positions);
        dataToAttributeList(1, 2, textureCoords);
        unbindVAO();
        return vaoID;
    }

    private void dataToAttributeList(int attributeIndex,int cordinateSize, float[] data){
        //Returns the earliest free index for VertexBufferList
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        vbos.add(vboID);
        //Has to convert the floatArray of data to a FloatBuffer since its the datatype in the VertexBufferList
        FloatBuffer floatBuffer = dataToFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeIndex, cordinateSize, GL11.GL_FLOAT, false, 0, 0);
        //if index = 0 (unbinds)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }



    private void bindIndicesBuffer(int[] indicies){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer intBuffer = dataToIntBuffer(indicies);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
    }

    public int loadTexture(String texturePath){
        Texture texture = null;
        try {
             texture = TextureLoader.getTexture("PNG", Files.newInputStream(Paths.get("res/texture/" + texturePath + ".png")));
             GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
             GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
             GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.1f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    private int createVAO(){
        //Returns the earliest free index for VertexArray
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        vaos.add(vaoID);
        return vaoID;
    }

    public void flushGL(){
        for(int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo : vbos){
            GL15.glDeleteBuffers(vbo);
        }

        for(int textureid : textures){
            GL11.glDeleteTextures(textureid);
        }

        textures.clear();

        vaos.clear();
        vbos.clear();
    }

    private void unbindVAO(){
        //Sets the current index in the Vertex Array for VAO object to 0
        GL30.glBindVertexArray(0);
    }

}
