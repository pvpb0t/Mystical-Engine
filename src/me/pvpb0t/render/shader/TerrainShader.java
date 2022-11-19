package me.pvpb0t.render.shader;

import me.pvpb0t.util.math.MatrixUtil;
import me.pvpb0t.world.Camera;
import me.pvpb0t.world.Light;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TerrainShader extends Shader{

    //Location of the transformMatrix in the gpu
    private int transformMatrixLocation;
    //Location of the projectionMatrix in the gpu
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPosLocation;
    private int lightColourLocation;
    private int shineFactorLocation;
    private int reflectionFactorLocation;
    private int skyColourLocation;
    private int backgroundTextureLocation;
    private int rTextureLocation;
    private int gTextureLocation;
    private int bTextureLocation;
    private int blendMapLocation;


    private static String vertexFile = "shaders/terrainVertexShader.vsh";
    private static String fragmentFile = "shaders/terrainFragmentShader.fsh";

    public TerrainShader() {
        super(vertexFile, fragmentFile);
    }

    @Override
    void getUniformLocations(){
        transformMatrixLocation = getUniformLocation("transformationMatrix");
        projectionMatrixLocation = getUniformLocation("projectionMatrix");
        viewMatrixLocation = getUniformLocation("viewMatrix");
        lightPosLocation = getUniformLocation("lightPosition");
        lightColourLocation = getUniformLocation("lightColour");
        shineFactorLocation = getUniformLocation("shineFactor");
        reflectionFactorLocation = getUniformLocation("reflectionFactor");
        skyColourLocation = getUniformLocation("skyColour");

        backgroundTextureLocation = getUniformLocation("backgroundTexture");
        rTextureLocation = getUniformLocation("rTexture");
        gTextureLocation = getUniformLocation("gTexture");
        bTextureLocation = getUniformLocation("bTexture");
        blendMapLocation = getUniformLocation("blendMap");

    }

    public void loadSpecularLight(float factor, float reflection){
        loadValue(shineFactorLocation, factor);
        loadValue(reflectionFactorLocation, reflection);
    }

    @Override
    void bindShader(){
        super.bindAttrib(0,"position");
        super.bindAttrib(1,"textureCoords");
        super.bindAttrib(2, "normal");
    }

    public void bindTextureContainer(){
        super.loadInt(backgroundTextureLocation, 0);
        super.loadInt(rTextureLocation, 1);
        super.loadInt(gTextureLocation, 2);
        super.loadInt(bTextureLocation, 3);
        super.loadInt(blendMapLocation, 4);

    }

    public void loadMatrix(Matrix4f matrix4f, MatrixType matrixType){
        switch (matrixType){
            case TRANSFORMATION:
                super.loadValue(transformMatrixLocation, matrix4f);
                break;
            case PROJECTION:
                super.loadValue(projectionMatrixLocation, matrix4f);
                break;
        }
    }

    public void loadLight(Light light){
        super.loadValue(lightPosLocation, light.getPosition());
        super.loadValue(lightColourLocation, light.getColour());
    }

    public void loadSkyColour(Vector3f color){
        super.loadValue(skyColourLocation, color);
    }



    public void loadMatrix(Camera camera){
        Matrix4f viewMatrix = MatrixUtil.createViewMatrix(camera);
        super.loadValue(viewMatrixLocation, viewMatrix);
    }

}
