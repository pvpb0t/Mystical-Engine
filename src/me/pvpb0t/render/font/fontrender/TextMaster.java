package me.pvpb0t.render.font.fontrender;

import me.pvpb0t.render.Loader;
import me.pvpb0t.render.font.mesh.FontType;
import me.pvpb0t.render.font.mesh.GUIText;
import me.pvpb0t.render.font.mesh.TextMeshData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMaster {
	
	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(Loader theLoader){
		renderer = new FontRenderer();
		loader = theLoader;
	}
	
	public static void render(){
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.injectToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			texts.remove(texts.get(text.getFont()));
		}
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}

}
