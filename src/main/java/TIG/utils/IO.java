package TIG.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import TIG.bundler.Bundle;
import TIG.characterSheet.CharacterSheet;
import TIG.features.Features;
import TIG.features.Field;
import TIG.features.Fields;
import TIG.items.Items;
import TIG.notes.NoteElement;
import TIG.scripts.Script;
import TIG.scripts.Scripts;
import TIG.tags.Tag;
import TIG.tags.Taggable;
import TIG.utils.gsonAdapters.CharacterSheetSerializer;
import TIG.utils.gsonAdapters.FeaturesSerializer;
import TIG.utils.gsonAdapters.FieldSerializer;
import TIG.utils.gsonAdapters.FieldsSerializer;
import TIG.utils.gsonAdapters.ItemsSerializer;
import TIG.utils.gsonAdapters.NoteElementSerializer;
import TIG.utils.gsonAdapters.ScriptSerializer;
import TIG.utils.gsonAdapters.ScriptsSerializer;
import TIG.utils.gsonAdapters.TagSerializer;
import TIG.utils.gsonAdapters.TaggableSerializer;

public class IO {

	private static final Logger LOG = LoggerFactory.getLogger(IO.class);

	private static Gson gson;
	
	static {
		// GSON initialization
		LOG.debug("Initializing GSON builder");
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(CharacterSheet.class, new CharacterSheetSerializer());
		builder.registerTypeAdapter(Features.class, new FeaturesSerializer());
		builder.registerTypeAdapter(Scripts.class, new ScriptsSerializer());
		builder.registerTypeAdapter(Script.class, new ScriptSerializer());
		builder.registerTypeAdapter(Taggable.class, new TaggableSerializer());
		builder.registerTypeAdapter(Tag.class, new TagSerializer());
		builder.registerTypeAdapter(Fields.class, new FieldsSerializer());
		builder.registerTypeAdapter(Field.class, new FieldSerializer());
		builder.registerTypeAdapter(Items.class, new ItemsSerializer());
		builder.registerTypeAdapter(NoteElement.class, new NoteElementSerializer());
		builder.setPrettyPrinting();
		builder.disableHtmlEscaping();
		IO.gson = builder.create();
		LOG.debug("Created GSON instance");
		
		// Character save directory initiliazation
		File characterSaveDir = new File(Def.characterDirPath);
		if(!characterSaveDir.exists()) {
			LOG.info("Creating character save directory");
			characterSaveDir.mkdirs();
			LOG.info("Character save directory: '" + characterSaveDir.getAbsolutePath() + "'");
		}
		
		// Bundle directory initialization
		File bundleDir = new File(Def.bundleDirPath);
		if(!bundleDir.exists()) {
			LOG.info("Creating bundle directory");
			bundleDir.mkdirs();
			LOG.info("Bundle directory: '" + bundleDir.getAbsolutePath() + "'");
		}
	}
	
	/**
	 * Saves a byte array to the specified path
	 * @param bytes - Data to save
	 * @param path - Absolute path to save file
	 * @return true on success, false otherwise
	 */
	private static boolean saveBytes(byte[] bytes, String path) {
		File saveFile = new File(path);
		LOG.debug("Saving data to '" + path + "'");
		try {
			if(!saveFile.exists()) {
				LOG.debug("Creating new save file");
				saveFile.createNewFile();
			}
			
			OutputStream os = new FileOutputStream(saveFile);
			os.write(bytes);
			os.flush();
			os.close();
			
			return true;
		} catch (IOException e) {
			LOG.error("Failed to save data to file '" + path + "'");
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
		LOG.debug("Loading data from '" + path + "'");
		try {
			return Files.readAllBytes(saveFile.toPath());
		} catch (IOException e) {
			LOG.error("Failed to read data from file");
			return null;
		}
	}
	
	/**
	 * Saves an object to the specified path
	 * @param obj - Object to serialize to JSON format
	 * @param path - Absolute path to the save file
	 * @return true on success, false otherwise
	 */
	private static boolean saveJSON(Object obj, String path) {
		LOG.debug("Saving object as json to '" + path + "'");
		return saveBytes(IO.gson.toJson(obj).getBytes(), path);
	}
	
	/**
	 * Saves a character sheet to the default character save path or in the specified location.
	 * This will force the use of the defined character save file extension. If a file path is given, any file extension
	 * the given will be ignored and replaced with the defined file extension.
	 * @param c - The character sheet to save
	 * @param path - The path to a directory, a file, or null to generate a default save file name in the default path
	 * @return true on success, false otherwise
	 */
	public static boolean saveCharacter(CharacterSheet c, String path) {
		
		// Ensure a valid file name
		String saveName = getFileName(path);
		if(saveName == null) {
			saveName = toFileName(c.getGeneralInfo().getCharacterName());
		}
		
		// Ensure the path exists
		String saveDir = getDirectoryPath(path);
		if(saveDir == null) {
			saveDir = Def.characterDirPath;
		} else if(!saveDir.equals(Def.characterDirPath)) {
			LOG.warn("Character '" + c.getGeneralInfo().getCharacterName() + "' is being saved to the non-standard location '" + saveDir + "'");
			File dir = new File(saveDir);
			if(!dir.exists()) {
				LOG.debug("Creating specified directories for character save '" + saveDir + "'");
				dir.mkdirs();
			}
		}
		
		LOG.info("Saving character sheet to '" + saveDir + saveName + Def.characterFileExtension + "'");
		return saveJSON(c, saveDir + saveName + Def.characterFileExtension);
	}
	
	/**
	 * Loads a character sheet from the given path to a character save file
	 * @param path - Path to the saved character sheet file.  The file must use the defined character save file extension.
	 * @return CharacterSheet on success, null otherwise
	 * @throws JsonSyntaxException - Indicates a malformed save file
	 */
	public static CharacterSheet loadCharacter(String path) throws JsonSyntaxException {
		// Ensure a valid file name
		String saveName = getFileName(path);
		if(saveName == null) {
			LOG.error("'" + path + "' is not a file path!");
			return null;
		}
		
		// Ensure the path exists
		String saveDir = getDirectoryPath(path);
		if(saveDir == null) {
			saveDir = Def.characterDirPath;
		}

		LOG.info("Loading character sheet from '" + saveDir + saveName + Def.characterFileExtension + "'");
		
		byte[] raw = loadBytes(saveDir + saveName + Def.characterFileExtension);
		if(raw == null) {
			LOG.error("Failed to read character sheet data from file '" + saveDir + saveName + Def.characterFileExtension + "'.  Does the file exist?");
			return null;
		}
		
		String rawJSON = new String(raw);
		LOG.debug("Getting character sheet from json");
		CharacterSheet output = IO.gson.fromJson(rawJSON, CharacterSheet.class);
		return output;
	}
	
	/**
	 * Loads a script from the path
	 * @param src_path - Absolute path to the script file (usually points to a script in a bundle)
	 * @return The raw source string of the script on success, null otherwise
	 */
	public static String loadScript(String src_path) {
		LOG.debug("Loading script from '" + src_path + "'");
		byte[] raw = loadBytes(src_path);
		if(raw == null) {
			LOG.error("Failed to read script data from file '" + src_path + "'");
			return null;
		}
		
		return new String(raw);
	}
	
	/**
	 * Loads a bundle from the path
	 * @param name - Name of the bundle in the bundle directory
	 * @return A Bundle object
	 */
	public static Bundle loadBundle(String name) {
		// TODO: Implement bundle loading
		return null;
	}
	
	/**
	 * Convert a string into a useable file name.  A file name must only contain the characters 
	 * [A-Za-z0-9_!@#$%^&()'{}[],`~-+=].
	 * @param rawName - A string potentially containing invalid characters and NOT containing any file extension or path
	 * @return A string containing only characters from the above character set
	 */
	public static String toFileName(String rawName) {
		String regex = "^" + Def.FILENAME_REGEX + "$";
		if(Pattern.matches(regex, rawName)) {
			return rawName;
		}else {
			LOG.debug("Attempting to convert invalid filename '" + rawName + "' into a valid form");
			String out = rawName.replaceAll(Def.FILENAME_REGEX_INV, "_");
			LOG.debug("Filename: '" + rawName + "' was converted to '" + out + "'");
			return out;
		}
	}
	
	/**
	 * Gets the directory path of a file/directory path
	 * @param path - Any file/directory path
	 * @return A string of just the directory portion of the given path or null if no parent directory is present
	 */
	public static String getDirectoryPath(String path) {
		if(path == null)
			return null;
		
		File tmp = new File(path);
		
		String out = null;
	
		// Get the parent directory if a file path is given
		String name = tmp.getName();
		if(name.matches(Def.FILE_REGEX)) {
			out = tmp.getParent();
			
		// Otherwise assume a directory path was given
		}else {
			out = path;
		}
		
		// Ensure the resultant path ends with a system separator
		if(out != null && !out.endsWith(File.separator)) {
			out += File.separator;
		}
		
		return out;
	}
	
	/**
	 * Gets the file name of a file/directory path
	 * @param path - Any file/directory path
	 * @return A string of the file name without an extension or null if the path points to a directory or this pathname's 
	 * name sequence is empty
	 */
	public static String getFileName(String path) {
		if(path == null)
			return null;
		
		File tmp = new File(path);
		String name = tmp.getName();
		if(name.matches(Def.FILE_REGEX)) {
			return name.substring(0, name.lastIndexOf('.'));
		}
		
		return null;
	}
	
	public static String loadView(String path) {
		if(path == null || path.isEmpty() || path.isBlank())
			return null;
		
		byte[] data = loadBytes(path);
		return new String(data);
	}
}
