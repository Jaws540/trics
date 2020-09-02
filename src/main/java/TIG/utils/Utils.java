package TIG.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import TIG.characterSheet.CharacterSheet;
import TIG.features.Features;
import TIG.features.Fields;
import TIG.items.Items;
import TIG.notes.NoteElement;
import TIG.scripts.Def;
import TIG.scripts.Scripts;
import TIG.tags.Tag;
import TIG.tags.Taggable;
import TIG.utils.gsonAdapters.FeaturesSerializer;
import TIG.utils.gsonAdapters.FieldsSerializer;
import TIG.utils.gsonAdapters.ItemsSerializer;
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
		builder.registerTypeAdapter(Items.class, new ItemsSerializer());
		builder.registerTypeAdapter(NoteElement.class, new NoteElementSerializer());
		builder.setPrettyPrinting();
		builder.disableHtmlEscaping();
		
		Utils.gson = builder.create();
	}
	
	/**
	 * Saves a byte array to the specified path
	 * @param bytes - Data to save
	 * @param path - Absolute path to save file
	 * @return true on success, false otherwise
	 */
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
			Log.error("Failed to save data to file!");
			return false;
		}
	}
	
	/**
	 * Loads a byte array from the specified path
	 * @param path - Absolute path to the saved file
	 * @return A byte array on success, null otherwise
	 */
	private static byte[] loadBytes(String path) {
		File saveFile = new File(path);
		try {
			return Files.readAllBytes(saveFile.toPath());
		} catch (IOException e) {
			Log.error("Failed to read data from file!  Path: '" + path + "'.");
			return null;
		}
	}
	
	/**
	 * Loads a character sheet from the given path to a character save file
	 * @param filePath - Absolute path to character sheet save file
	 * @return CharacterSheet on success, null otherwise
	 * @throws JsonSyntaxException - Indicates a malformed save file
	 */
	public static CharacterSheet loadCharacter(String filePath) throws JsonSyntaxException {
		byte[] raw = loadBytes(filePath);
		if(raw == null)
			return null;
		
		String rawJSON = new String(raw);
		CharacterSheet output = Utils.gson.fromJson(rawJSON, CharacterSheet.class);
		return output;
	}
	
	/**
	 * Saves an object to the specified path
	 * @param obj - Object to serialize to JSON format
	 * @param path - Absolute path to the save file
	 * @return true on success, false otherwise
	 */
	public static boolean saveJSON(Object obj, String path) {
		return saveBytes(Utils.gson.toJson(obj).getBytes(), path);
	}
	
	/**
	 * Loads a script from the path
	 * @param src_path - Absolute path to the script file
	 * @return The raw source string of the script on success, null otherwise
	 */
	public static String loadScript(String src_path) {
		byte[] raw = loadBytes(src_path);
		if(raw == null)
			return null;
		
		return new String(raw);
	}
	
	/**
	 * Validates an ID for use as a valid identifier in the program and for recognizability in scripts
	 * @param id - ID to test for validity
	 * @return A valid identifier.  If the id given is already a valid identifier, it will simply be returned.  If 
	 * an invalid identifier is given, this will return a valid identifier based on the given id.
	 */
	public static String validateID(String id) {
		String regex = "^" + Def.ID_REGEX + "$";
		if(Pattern.matches(regex, id)) {
			return id;
		}else {
			String out = id.replaceAll("[^a-zA-Z0-9_:]", "");
			if(!Pattern.matches(regex, out))
				out = "__" + out;
			Log.warn("Invalid identifier.  '" + id + "' is not a valid ID.  Modified replacement '" + out + "'.");
			return out;
		}
	}

}
