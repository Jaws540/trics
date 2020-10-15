package TIG.utils;

import java.io.File;

public class Def {
	
	public static final String TITLE = "RPG Character Sheet";
	
	// Fields with all caps should not be changed
	// Fields with lowercase may be changed

	// Regular Expressions
		public static final String ID_REGEX = "[a-zA-Z_][a-zA-Z0-9_:]*";
		public static final String ID_REGEX_INV = "[^a-zA-Z0-9_:]";
		// Just escape all individual characters to avoid unintentional side-effects
		public static final String FILENAME_REGEX = "[A-Za-z0-9\\_\\!\\@\\#\\$\\%\\^\\&\\(\\)'\\{\\}\\[\\]\\,\\`\\~\\-\\+\\=]+";
		public static final String FILENAME_REGEX_INV = "[^A-Za-z0-9\\_\\!\\@\\#\\$\\%\\^\\&\\(\\)'\\{\\}\\[\\]\\,\\`\\~\\-\\+\\=]";
		public static final String FILE_REGEX = "^.+\\..+$";
	
	// Type names
		public static final String INT = "integer";
		public static final String DOUBLE = "double";
		public static final String BOOL = "boolean";
		public static final String STRING = "string";
	
	// General reserved identifiers
		public static final String INFO = "Info";
		public static final String FEATURES = "Features";
		public static final String INVENTORY = "Inventory";
		public static final String ITEMS = "Items";
	
	// Specific reserved identifiers
		public static final String PLAYER_NAME = "playerName";
		public static final String CHARACTER_NAME = "characterName";
		public static final String DISPLAY_NAME = "displayName";
		public static final String DESCRIPTION = "description";
		public static final String SCRIPTS = "scripts";
	
	// Standard Library Functions
		public static final String DISPLAY = "display";
		
	// File stuff
		public static final String bundleDirPath = "bundles" + File.separator;
		public static final String characterDirPath = "characters" + File.separator;

		public static final String scriptFileExtension = ".rpgs";
		public static final String characterFileExtension = ".rpgc";
		public static final String bundleFileExtension = ".rpgb";

}
