package me.pvpb0t.render.tesselator;

import me.pvpb0t.render.shader.MatrixType;
import me.pvpb0t.render.shader.TerrainShader;
import me.pvpb0t.world.Camera;
import me.pvpb0t.world.entity.EntityBase;
import me.pvpb0t.world.Light;
import me.pvpb0t.render.model.TexturedModel;
import me.pvpb0t.render.shader.ShaderCloseType;
import me.pvpb0t.render.shader.ShaderMain;
import me.pvpb0t.world.terrain.Terrain;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MainTesselator extends Tesselator{
    //ADD CFG FOR THIS SHIT
    private static float FOV = 90;
    private static final float MinDistance = 0.1f;
    private static final float MaxDistance = 4000f;

    private Vector3f skyColour = new Vector3f(0.215f, 0.2588f, 0.3568f);

    private Matrix4f projectionMatrix;
    private ShaderMain shader = new ShaderMain();
    private EntityTesselator entityTesselator;
    private TerrainTessalator terrainTessalator;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<EntityBase>> toRender= new HashMap<>();
    private ArrayList<Terrain> terrains = new ArrayList<>();

    public MainTesselator(){
        //ENABLE CULLING TO NOT RENDER VERTECIES INSIDE OBJECT
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
       entityTesselator = new EntityTesselator(shader, projectionMatrix);
       terrainTessalator = new TerrainTessalator(terrainShader, projectionMatrix);
    }

    public void render(Light sun, Camera cam){
        this.prepare();
        shader.start();
        shader.loadSkyColour(skyColour);
        shader.loadLight(sun);
        shader.loadMatrix(cam);
        entityTesselator.render(toRender);
        shader.close(ShaderCloseType.UNSAFE);

        terrainShader.start();
        terrainShader.loadSkyColour(skyColour);
        terrainShader.loadLight(sun);
        terrainShader.loadMatrix(cam);
        terrainTessalator.render(terrains);
        terrainShader.close(ShaderCloseType.UNSAFE);

        terrains.clear();
        toRender.clear();
    }

    public void processEntityBase(EntityBase entityBase){
        TexturedModel texturedModel = entityBase.getModel();
        List<EntityBase> entityBases = toRender.get(texturedModel);
        if(entityBases!=null){
            entityBases.add(entityBase);
        }else {
                ArrayList<EntityBase> newList = new ArrayList<>();
                newList.add(entityBase);
                toRender.put(texturedModel, newList);
        }
    }

    public void proccessTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void flush(){
        shader.close(ShaderCloseType.SAFE);
        terrainShader.close(ShaderCloseType.SAFE);
    }

    public Map<TexturedModel, List<EntityBase>> getToRender() {
        return toRender;
    }

    public void prepare(){
        //Clears all Color Buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        //Clears all Depth Buffer
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        //Clears all Stencil Buffer
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        //Clears all Accumulation Buffer
        GL11.glClear(GL11.GL_ACCUM_BUFFER_BIT );
        //Color Rainbow = ColorUtil.rainbow(5);
        GL11.glClearColor(skyColour.getX(), skyColour.getY(),skyColour.getZ(),1);
    }

    public void addFOV(float ammount){
        FOV+=ammount;
        if(FOV<0){
            FOV=0;
        }else if(FOV>=179){
            FOV=179;
        }
    }


    //https://youtu.be/50Y9u7K0PZo - 2022/09/01
    private void createProjectionMatrix(){
        //ADD CONFIG FOR THIS
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = MaxDistance - MinDistance;

        projectionMatrix= new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((MaxDistance + MinDistance) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * MinDistance * MaxDistance) / frustum_length);
        projectionMatrix.m33 = 0;
    }

}
