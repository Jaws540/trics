package TIG.utils.io;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

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
import TIG.utils.Def;
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

public class CharacterIO {

	private static final Logger LOG = LoggerFactory.getLogger(CharacterIO.class);
	
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
		CharacterIO.gson = builder.create();
		LOG.debug("Created GSON instance");
		
		// Character save directory initiliazation
		File characterSaveDir = new File(Def.characterDirPath);
		if(!characterSaveDir.exists()) {
			LOG.info("Creating character save directory");
			characterSaveDir.mkdirs();
			LOG.info("Character save directory: '" + characterSaveDir.getAbsolutePath() + "'");
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
		return IO.saveBytes(CharacterIO.gson.toJson(obj).getBytes(), path);
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
		String saveName = IO.getFileName(path);
		if(saveName == null) {
			saveName = IO.toFileName(c.getGeneralInfo().getCharacterName());
		}
		
		// Ensure the path exists
		String saveDir = IO.getDirectoryPath(path);
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
		String saveName = IO.getFileName(path);
		if(saveName == null) {
			LOG.error("'" + path + "' is not a file path!");
			return null;
		}
		
		// Ensure the path exists
		String saveDir = IO.getDirectoryPath(path);
		if(saveDir == null) {
			saveDir = Def.characterDirPath;
		}

		LOG.info("Loading character sheet from '" + saveDir + saveName + Def.characterFileExtension + "'");
		
		byte[] raw = IO.loadBytes(saveDir + saveName + Def.characterFileExtension);
		if(raw == null) {
			LOG.error("Failed to read character sheet data from file '" + saveDir + saveName + Def.characterFileExtension + "'.  Does the file exist?");
			return null;
		}
		
		String rawJSON = new String(raw);
		LOG.debug("Getting character sheet from json");
		CharacterSheet output = CharacterIO.gson.fromJson(rawJSON, CharacterSheet.class);
		return output;
	}

}
