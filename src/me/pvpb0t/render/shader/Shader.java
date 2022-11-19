package me.pvpb0t.render.shader;

import me.pvpb0t.util.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class Shader {


    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;
    private static FloatBuffer floatBuffer= BufferUtils.createFloatBuffer(16);


    public Shader(String vertexFile, String fragmentFile){
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindShader();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getUniformLocations();
    }

    void getUniformLocations(){

    }



    int getUniformLocation(String location){
        return GL20.glGetUniformLocation(programID,location);
    }


    //STORES/CHAGE THE VALUES TO A LOCATION IN THE SHADER SCRIPT
    protected void loadValue(int location, float value){
        GL20.glUniform1f(location,value);
    }

    protected void loadValue(int location, boolean value){
        GL20.glUniform1f(location, value?1:0);

    }

    public void loadInt(int location, int value){
        GL20.glUniform1i(location, value);
    }

    protected void loadValue(int location, Matrix4f value){
        value.store(floatBuffer);
        floatBuffer.flip();
        GL20.glUniformMatrix4(location, false, floatBuffer);

    }
    protected void loadValue(int location, Vector3f vector3f){
        GL20.glUniform3f(location, vector3f.x,vector3f.y,vector3f.z);
    }

    protected void load2DVector(int location, Vector2f vector){
        GL20.glUniform2f(location,vector.x,vector.y);
    }
    public void start(){
        if(programID!= 0){
            GL20.glUseProgram(programID);
        }else{
            Logger.print("Shader not initialize");
        }
    }

    void bindShader(){
        Logger.print("Binding shader");
    }

    void bindAttrib(int attribute, String name){
        GL20.glBindAttribLocation(programID,attribute,name);

    }

    public void close(ShaderCloseType closeType){
        if(closeType.equals(ShaderCloseType.UNSAFE)){
            GL20.glUseProgram(0);
        }else if(closeType.equals(ShaderCloseType.SAFE)){
            int[] shadersID = {vertexShaderID, fragmentShaderID};
            GL20.glUseProgram(0);
            for(int i: shadersID){
                GL20.glDetachShader(programID, i);
                GL20.glDeleteShader(i);
            }
            GL20.glDeleteProgram(programID);

        }
    }


    public int loadShader(String file, int type) {
        //Läser filen och bygger på varje line på en string. Sedan skapas en ny shader med hjälp av openGL och källkoden för scripten blir injekterad.
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;


    }



}
