package me.pvpb0t.render.model;

import me.pvpb0t.render.model.raw.Texture;
import me.pvpb0t.render.model.raw.VaoObject;

public class TexturedModel {

    private Texture object;
    private VaoObject vaoObject;

    public Texture getTexture() {
        return object;
    }

    public void setTexture(Texture object) {
        this.object = object;
    }

    public VaoObject getVaoObject() {
        return vaoObject;
    }

    public void setVaoObject(VaoObject vaoObject) {
        this.vaoObject = vaoObject;
    }

    public TexturedModel(Texture object, VaoObject vaoObject) {
        this.object = object;
        this.vaoObject = vaoObject;
    }
}
