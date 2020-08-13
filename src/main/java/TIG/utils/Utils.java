package TIG.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import TIG.characterSheet.CharacterSheet;
import TIG.features.Features;
import TIG.features.Fields;
import TIG.scripts.Scripts;
import TIG.tags.Tag;
import TIG.tags.Taggable;
import TIG.utils.gsonAdapters.FeaturesSerializer;
import TIG.utils.gsonAdapters.FieldsSerializer;
import TIG.utils.gsonAdapters.ScriptsSerializer;
import TIG.utils.gsonAdapters.TagSerializer;
import TIG.utils.gsonAdapters.TaggableSerializer;

public class Utils {
	
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
	
	public static CharacterSheet loadCharacter(String filePath) {
		// TODO: Load character and all the subcomponents
		return null;
	}
	
	public static boolean saveJSON(Object obj, String path) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Features.class, new FeaturesSerializer());
		builder.registerTypeAdapter(Scripts.class, new ScriptsSerializer());
		builder.registerTypeAdapter(Taggable.class, new TaggableSerializer());
		builder.registerTypeAdapter(Tag.class, new TagSerializer());
		builder.registerTypeAdapter(Fields.class, new FieldsSerializer());
		builder.setPrettyPrinting();
		
		Gson gson = builder.create();
		saveBytes(gson.toJson(obj).getBytes(), path);
		return false;
	}

}
