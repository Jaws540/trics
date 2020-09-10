/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TIG;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TIG.characterSheet.CharacterSheet;
import TIG.gui.CharacterSheetPane;
import TIG.scripts.compiler.Interpreter;
import TIG.utils.TestData;
import TIG.utils.Utils;

public class App {
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	private static final String testSavePath = "res\\";
	private static final String characterSavePath = testSavePath + "testSave2.json";

    public static void main(String[] args) {
    	LOG.info("Application started");
    	//runGuiTest(args);
    	runCharacterTests();
    }
    
    @SuppressWarnings("unused")
    private static void runGuiTest(String[] args) {
    	CharacterSheetPane.main(args);
    }
    
	private static void runCharacterTests() {
    	LOG.debug("Saving test data");
    	Utils.saveJSON(TestData.testCharacter, characterSavePath);

    	CharacterSheet character = null;
    	try {
    		character = Utils.loadCharacter(characterSavePath);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    	LOG.debug("Loading and running interpreter on test script");
    	Interpreter interp = new Interpreter("res\\TestScript1.cis", character);
    	interp.compile();
    	interp.run();

    	LOG.debug("Saving modified test data");
    	Utils.saveJSON(character, testSavePath + "testSave3.json");
    }
}
