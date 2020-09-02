/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TIG;

import TIG.characterSheet.CharacterSheet;
import TIG.scripts.compiler.Interpreter;
import TIG.utils.Utils;

public class App {
	
	private static final String testSavePath = "D:\\Users\\Jacob\\Coding\\Java\\RPGIS\\RPG-Integrated-System\\res\\";
	private static final String characterSavePath = testSavePath + "testSave2.json";

    public static void main(String[] args) {
    	CharacterSheet character = null;
    	try {
    		character = Utils.loadCharacter(characterSavePath);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	Interpreter interp = new Interpreter("D:\\Users\\Jacob\\Coding\\Java\\RPGIS\\RPG-Integrated-System\\res\\TestScript1.cis", character);
    	interp.compile();
    	interp.run();
    }
}
