package me.pvpb0t.render.shader;

import me.pvpb0t.render.shader.Shader;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;


public class FontShader extends Shader {

	private static final String VERTEX_FILE = "shaders/fontVertex.vsh";
	private static final String FRAGMENT_FILE = "shaders/fontFragment.fsh";

	private int location_colour;
	private int location_translation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	void getUniformLocations(){
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
	}

	@Override
	void bindShader(){
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoords");
	}
	
	public void loadColour(Vector3f colour){
		super.loadValue(location_colour, colour);
	}

	public void loadTranslation(Vector2f translation){
		super.load2DVector(location_translation, translation);
	}


}
