package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import characterSheet.CharacterSheet;

public class CharacterUtils {
	
	public static CharacterSheet loadCharacter(String filePath) {
		return null;
	}
	
	public static boolean saveCharacter(CharacterSheet character, String path) {
		File saveFile = new File(path);
		try {
			if(!saveFile.exists())
				saveFile.createNewFile();
			
			PrintWriter pw = new PrintWriter(saveFile);
			pw.print(character.toJSON(1));
			pw.flush();
			pw.close();
			return true;
		} catch (IOException e) {
			// TODO: Log file not found
			e.printStackTrace();
			return false;
		}
	}

}
