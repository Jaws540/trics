/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TIG;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TIG.characterSheet.CharacterSheet;
import TIG.gui.GUI;
import TIG.utils.Def;
import TIG.utils.TestData;
import TIG.utils.io.BundleIO;
import TIG.utils.io.CharacterIO;

public class App {
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
    	LOG.debug("Application started");
    	BundleIO.loadBundles();
    	runGuiTest(args);
    	runCharacterTests();
    }
    
    @SuppressWarnings("unused")
    private static void runGuiTest(String[] args) {
    	GUI.main(args);
    }

    @SuppressWarnings("unused")
	private static void runCharacterTests() {
    	String characterSavePath = TestData.getTestCharacter().getGeneralInfo().getCharacterName() + Def.characterFileExtension;
    	
    	LOG.debug("Saving test data");
    	CharacterIO.saveCharacter(TestData.getTestCharacter(), null);

    	CharacterSheet character = null;
    	try {
    		character = CharacterIO.loadCharacter(characterSavePath);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    	LOG.debug("Loading and running interpreter on test script");
    	/*Interpreter interp = new Interpreter("res\\TestScript1.rpgs", character);
    	interp.compile();
    	interp.run();*/
    	character.features.getFeature("Strength").scripts.getScript("test_script").run(character);

    	LOG.debug("Saving modified test data");
    	CharacterIO.saveCharacter(character, "PostScriptTestSave" + Def.characterFileExtension);
    }
}
