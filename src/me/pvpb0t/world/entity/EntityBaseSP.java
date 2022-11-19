package me.pvpb0t.world.entity;

import me.pvpb0t.EngineCore;
import me.pvpb0t.render.model.TexturedModel;
import me.pvpb0t.util.KeyboardUtil;
import me.pvpb0t.util.Logger;
import me.pvpb0t.world.Camera;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EntityBaseSP extends EntityBase{

    private float yaw, pitch;
    private final float defaultMotion=0.7f;
    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void Gravity(float factor, float terminal_velocity){
        motionY+=factor;
        if(motionY>terminal_velocity){
            motionY=terminal_velocity;
        }
        this.setPosition(new Vector3f(position.x, position.y-motionY,position.z));
    }
    public EntityBaseSP(TexturedModel model, float rotationX, float rotationY, float rotationZ, Vector3f scale, Vector3f position) {
        super(model, rotationX, rotationY, rotationZ, scale, position, false, true, 0f);
    }

    public float getDistance(Vector3f pointOne, Vector3f pointTwo) {
        float distance = 0;

        float x1 = pointOne.x;
        float y1 = pointOne.y;
        float z1 = pointOne.z;

        float x2 = pointTwo.x;
        float y2 = pointTwo.y;
        float z2 = pointTwo.z;

        distance = (float) Math.pow((Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2)), .5f);

        return distance;
    }



    public void invertMotion(float motionX, float motionZ){
        this.motionX-=2*motionX;
        this.motionZ-=2*motionZ;
    }

    public void addY(float value, float motionY){
        position.y+=value;
        this.motionY+=motionY;
    }

    private boolean isStrafing;

    public boolean isStrafing() {
        return isStrafing;
    }

    public void setStrafing(boolean strafing) {
        isStrafing = strafing;
    }

    public float getMotionY() {
        return motionY;
    }

    public void setMotionY(float fallingspeed) {
        this.motionY = fallingspeed;
    }

    private float motionY=0;
    private float motionX=0.7f;
    private float motionZ=0.7f;
    private boolean isfalling=false;
    private boolean isMoved=false;


    public boolean isIsfalling() {
        return isfalling;
    }

    public void setIsfalling(boolean isfalling) {
        this.isfalling = isfalling;
    }
    private Camera camera;

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void allignCamera(){
        camera.setPitch(pitch);
        camera.setYaw(yaw);
    }

    public float getMotionX() {
        return motionX;
    }

    public void setMotionX(float motionX) {
        this.motionX = motionX;
    }

    public float getMotionZ() {
        return motionZ;
    }

    public void setMotionZ(float motionZ) {
        this.motionZ = motionZ;
    }

    public boolean checkCollision(EntityBase e, Vector3f pos){
        if (this.getDistance(pos, e.getPosition()) < e.getRadius()) {
            return true;
        }
    return false;}

    public boolean checkCollisionNoY(EntityBase e, Vector3f pos){
        if (this.getDistance(new Vector3f(pos.x, 0, pos.z), new Vector3f(e.position.x, 0, e.position.z)) < e.getRadius()) {
            return true;
        }
        return false;}

    public boolean isMoved() {
        return isMoved;
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
    }

    public void move(EntityBase e){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            double x = Math.cos(Math.toRadians(yaw + 90.0f));
            double z = Math.cos(Math.toRadians(yaw));
            float nextX =(float) (position.getX() - x*motionX);
            float nextZ =  (float) (position.getZ() - z*motionZ);
            if(!checkCollisionNoY(e, new Vector3f(nextX, this.getPosition().y,nextZ)) || e.passThrough){
                position.setX(nextX);
                position.setZ(nextZ);
            }else{
                this.invertMotion(defaultMotion, defaultMotion);
            }
            isMoved=true;


            // position.z-=0.2f;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            double x = Math.cos(Math.toRadians(yaw + 180));
            double z = Math.cos(Math.toRadians(yaw+90));
            position.setX((float) (position.getX() - x*0.7));
            position.setZ((float) (position.getZ() - z*0.7));
            isMoved=true;
            // position.z-=0.2f;
        }

        if(!isStrafing) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                motionZ = 2;
                motionX = 2;
            } else {
                motionZ = 0.7f;
                motionX = 0.7f;
            }
        }


        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            isMoved=true;

            double x = Math.cos(Math.toRadians(yaw));
            double z = Math.cos(Math.toRadians(yaw-90));
            position.setX((float) (position.getX()  -x*0.7));
            position.setZ((float) (position.getZ() - z*0.7));
            // position.z-=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            isMoved=true;

            double x = Math.cos(Math.toRadians(yaw + 90.0f));
            double z = Math.cos(Math.toRadians(yaw));
            position.setX((float) (position.getX() + x*0.7));
            position.setZ((float) (position.getZ() + z*0.7));
        }if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            isStrafing=true;
            if(!isfalling){
                position.y+=0.9f;
                motionY=-0.37f;
                motionX+=0.2f;
                motionZ+=0.2f;
            }
        }else{
            isStrafing=false;

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            yaw-=3.5f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            yaw+=3.5f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
            pitch-=2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            pitch+=2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_R)){
            EngineCore.onClose();
            EngineCore.main(null);
        }
    }
}
