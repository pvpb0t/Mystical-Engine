package me.pvpb0t.render.model.raw;

public class Texture {

    private int textureID;
    private float shineFactor = 1;
    private float reflectionFactor =0;

    public Texture(int textureID, float shineFactor, float reflectionFactor) {
        this.textureID = textureID;
        this.shineFactor = shineFactor;
        this.reflectionFactor = reflectionFactor;
    }

    public float getShineFactor() {
        return shineFactor;
    }

    public void setShineFactor(float shineFactor) {
        this.shineFactor = shineFactor;
    }

    public float getReflectionFactor() {
        return reflectionFactor;
    }

    public void setReflectionFactor(float reflectionFactor) {
        this.reflectionFactor = reflectionFactor;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public Texture(int textureID) {
        this.textureID = textureID;
    }
}
