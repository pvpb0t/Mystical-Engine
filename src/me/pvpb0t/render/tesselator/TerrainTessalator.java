package me.pvpb0t.render.tesselator;

import me.pvpb0t.render.model.TexturedModel;
import me.pvpb0t.render.model.raw.Texture;
import me.pvpb0t.render.model.raw.VaoObject;
import me.pvpb0t.render.shader.MatrixType;
import me.pvpb0t.render.shader.ShaderCloseType;
import me.pvpb0t.render.shader.TerrainShader;
import me.pvpb0t.util.math.MatrixUtil;
import me.pvpb0t.world.entity.EntityBase;
import me.pvpb0t.world.terrain.Terrain;
import me.pvpb0t.world.terrain.TerrainTextureContainer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

public class TerrainTessalator {

    private TerrainShader shader;

    public TerrainTessalator(TerrainShader shader, Matrix4f projectMatrix){
        this.shader = shader;
        shader.start();
        shader.loadMatrix(projectMatrix, MatrixType.PROJECTION);
        shader.bindTextureContainer();
        shader.close(ShaderCloseType.UNSAFE);
    }

    public void render(ArrayList<Terrain> terrains){
        for(Terrain terrain : terrains){
            prepareTerrain(terrain);
            prepearTerrainInstance(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getVaoObject().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindtexturedModel();
        }
    }

    public void prepareTerrain(Terrain terrain){
        VaoObject rawModel = terrain.getVaoObject();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0); // position
        GL20.glEnableVertexAttribArray(1); // textureCoordinates
        GL20.glEnableVertexAttribArray(2); // normal
        bindTextures(terrain);
        shader.loadSpecularLight(1, 0 );

    }

    private void bindTextures(Terrain terrain){
        TerrainTextureContainer textureContainer = terrain.getTextureContainer();
        //Slot 0
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureContainer.getBackgroundTexture().getTextureID());
        //Slot 1
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureContainer.getrTexture().getTextureID());
        //Slot2
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureContainer.getgTexture().getTextureID());
        //Slot3
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureContainer.getbTexture().getTextureID());
        //Slot4
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    private void unbindtexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepearTerrainInstance(Terrain entity)
    {
        Matrix4f transformationMatrix = MatrixUtil.createTransformationMatrix(new Vector3f(entity.getX(), 0, entity.getZ()),0, 0, 0,new Vector3f(1,1,1));
        shader.loadMatrix(transformationMatrix, MatrixType.TRANSFORMATION);
    }

}
