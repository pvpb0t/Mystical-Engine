package me.pvpb0t.world;

import me.pvpb0t.EngineCore;
import me.pvpb0t.util.DebugUtil;
import me.pvpb0t.util.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f();
    private float pitch, yaw, roll;


    public Vector3f getPosition() {
        return position;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }


    public Camera(Vector3f position, float pitch, float yaw, float roll) {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }
    public Camera(){

    }
}
