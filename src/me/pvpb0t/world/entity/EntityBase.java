package me.pvpb0t.world.entity;

import me.pvpb0t.render.model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class EntityBase {

    protected TexturedModel model;
    protected float rotationX, rotationY, rotationZ;
    protected Vector3f scale, position;
    protected boolean passThrough;

    private float radius;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean isPassThrough() {
        return passThrough;
    }

    public EntityBase(TexturedModel model, float rotationX, float rotationY, float rotationZ, Vector3f scale, Vector3f position, boolean solid, boolean passThrough, float radius) {
        this.model = model;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.scale = scale;
        this.position = position;
        if(solid){
            EntityManager.addEntities(this);
        }
        this.passThrough=passThrough;
        this.radius=radius;
    }



    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public float getRotationX() {
        return rotationX;
    }

    public void setRotationX(float rotationX) {
        this.rotationX = rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationZ(float rotationZ) {
        this.rotationZ = rotationZ;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
