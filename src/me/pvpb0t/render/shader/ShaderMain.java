package me.pvpb0t.render.shader;

import me.pvpb0t.world.Camera;
import me.pvpb0t.world.Light;
import me.pvpb0t.util.math.MatrixUtil;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class ShaderMain extends Shader
{
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

    private static String vertexFile = "shaders/vertexShader.vsh";
    private static String fragmentFile = "shaders/fragmentShader.fsh";

    public ShaderMain() {
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

    public void loadSkyColour(Vector3f color){
        super.loadValue(skyColourLocation, color);
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

    public void loadMatrix(Camera camera){
        Matrix4f viewMatrix = MatrixUtil.createViewMatrix(camera);
        super.loadValue(viewMatrixLocation, viewMatrix);
    }
}
