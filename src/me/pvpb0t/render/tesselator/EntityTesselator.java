package me.pvpb0t.render.tesselator;

import me.pvpb0t.world.entity.EntityBase;
import me.pvpb0t.render.model.TexturedModel;
import me.pvpb0t.render.model.raw.Texture;
import me.pvpb0t.render.model.raw.VaoObject;
import me.pvpb0t.render.shader.MatrixType;
import me.pvpb0t.render.shader.ShaderCloseType;
import me.pvpb0t.render.shader.ShaderMain;
import me.pvpb0t.util.math.MatrixUtil;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EntityTesselator extends Tesselator{

    //ADD THIS TO CONFIG FILE TYPE SHIT

    private ShaderMain shader;





    public EntityTesselator(ShaderMain shader, Matrix4f projectionMatrix){
        //UTESLUTER FACES SOM ÄR ROTATERADE BORT FRÅN KAMERAN FÖR PERFORMANCE
        this.shader = shader;
        shader.start();
        shader.loadMatrix(projectionMatrix, MatrixType.PROJECTION);
        shader.close(ShaderCloseType.UNSAFE);
    }



    public void render(Map<TexturedModel, List<EntityBase>> entities){
        for(TexturedModel model:entities.keySet()){
            prepareTexturedModel(model);
            List<EntityBase> batch = entities.get(model);
            for(EntityBase entity:batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVaoObject().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindtexturedModel();
        }
    }

    public void prepareTexturedModel(TexturedModel model){
        VaoObject rawModel = model.getVaoObject();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0); // position
        GL20.glEnableVertexAttribArray(1); // textureCoordinates
        GL20.glEnableVertexAttribArray(2); // normal
        Texture texture = model.getTexture();
        shader.loadSpecularLight(texture.getShineFactor(), texture.getReflectionFactor() );
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void unbindtexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(EntityBase entity)
    {
        Matrix4f transformationMatrix =MatrixUtil.createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
        shader.loadMatrix(transformationMatrix, MatrixType.TRANSFORMATION);
    }




}
