package TIG.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import TIG.characterSheet.CharacterSheet;
import TIG.features.Features;
import TIG.features.Fields;
import TIG.notes.NoteElement;
import TIG.scripts.Scripts;
import TIG.tags.Tag;
import TIG.tags.Taggable;
import TIG.utils.gsonAdapters.FeaturesSerializer;
import TIG.utils.gsonAdapters.FieldsSerializer;
import TIG.utils.gsonAdapters.NoteElementSerializer;
import TIG.utils.gsonAdapters.ScriptsSerializer;
import TIG.utils.gsonAdapters.TagSerializer;
import TIG.utils.gsonAdapters.TaggableSerializer;

public class Utils {
	
	private static Gson gson;
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Features.class, new FeaturesSerializer());
		builder.registerTypeAdapter(Scripts.class, new ScriptsSerializer());
		builder.registerTypeAdapter(Taggable.class, new TaggableSerializer());
		builder.registerTypeAdapter(Tag.class, new TagSerializer());
		builder.registerTypeAdapter(Fields.class, new FieldsSerializer());
		builder.registerTypeAdapter(NoteElement.class, new NoteElementSerializer());
		builder.setPrettyPrinting();
		builder.disableHtmlEscaping();
		
		Utils.gson = builder.create();
	}
	
	private static boolean saveBytes(byte[] bytes, String path) {
		File saveFile = new File(path);
		try {
			if(!saveFile.exists())
				saveFile.createNewFile();
			
			OutputStream os = new FileOutputStream(saveFile);
			os.write(bytes);
			os.flush();
			os.close();
			
			return true;
		} catch (IOException e) {
			// TODO: Log errors
			e.printStackTrace();
			return false;
		}
	}
	
	private static byte[] loadBytes(String path) {
		File saveFile = new File(path);
		try {
			return Files.readAllBytes(saveFile.toPath());
		} catch (IOException e) {
			// TODO: Log errors
			e.printStackTrace();
			return null;
		}
	}
	
	public static CharacterSheet loadCharacter(String filePath) throws JsonSyntaxException {
		byte[] raw = loadBytes(filePath);
		if(raw == null )
			return null;
		
		String rawJSON = new String(raw);
		CharacterSheet output = Utils.gson.fromJson(rawJSON, CharacterSheet.class);
		return output;
	}
	
	public static boolean saveJSON(Object obj, String path) {
		return saveBytes(Utils.gson.toJson(obj).getBytes(), path);
	}

}
