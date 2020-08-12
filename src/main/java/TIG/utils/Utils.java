package TIG.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import TIG.characterSheet.CharacterSheet;

public class Utils {
	
	public static CharacterSheet loadCharacter(String filePath) {
		// TODO: Load character and all the subcomponents
		return null;
	}
	
	public static boolean saveJSON(JSONify obj, String path) {
		File saveFile = new File(path);
		try {
			if(!saveFile.exists())
				saveFile.createNewFile();
			
			PrintWriter pw = new PrintWriter(saveFile);
			pw.print(obj.toJSON(1));
			pw.flush();
			pw.close();
			return true;
		} catch (IOException e) {
			// TODO: Log errors
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean saveGSON(Object obj, String path) {
		Gson gson = new Gson();
		return false;
	}

}
