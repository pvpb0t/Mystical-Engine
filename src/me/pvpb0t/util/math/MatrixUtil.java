package me.pvpb0t.util.math;

import me.pvpb0t.world.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MatrixUtil {

public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, Vector3f scale){
    Matrix4f matrix4f = new Matrix4f();
    matrix4f.setIdentity();
    Matrix4f.translate(translation, matrix4f, matrix4f);
    //ROTATE X
    Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0),matrix4f,matrix4f);
    //ROTATE Y
    Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0),matrix4f,matrix4f);
    //ROTATE Z
    Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1),matrix4f,matrix4f);
    //SCALE
    Matrix4f.scale(scale,matrix4f,matrix4f);
    return matrix4f;
}

//INSPIRATION FROM https://youtu.be/50Y9u7K0PZo but updated - 2022/9/2
public static Matrix4f createViewMatrix(Camera camera){
    Matrix4f viewMatrix = new Matrix4f();
    viewMatrix.setIdentity();
    Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0),viewMatrix,viewMatrix);
    Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0),viewMatrix,viewMatrix);
    Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1),viewMatrix,viewMatrix);
    Vector3f position = camera.getPosition();
    Vector3f negativePos = new Vector3f(-position.x, -position.y, -position.z);
    Matrix4f.translate(negativePos, viewMatrix, viewMatrix);
    return viewMatrix;
}
}
