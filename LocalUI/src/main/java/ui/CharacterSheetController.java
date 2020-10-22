package ui;

import java.io.File;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import characterModel.characterSheet.CharacterSheet;
import characterModel.scripts.Entry;
import characterModel.scripts.Environment;
import characterModel.utils.exceptions.interpreterExceptions.ExistenceException;
import characterModel.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;
import characterModel.utils.io.CharacterIO;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class CharacterSheetController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CharacterSheetController.class);
	
	private CharacterSheet sheet = null;
	
	private final Stage mainStage;
	private final CharacterSheetPane csView;
	
	public CharacterSheetController(Stage mainStage, CharacterSheetPane csView) {
		this.mainStage = mainStage;
		this.csView = csView;
	}
	
	public boolean loadSheet() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Load Character Sheet");
		chooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Character Sheet", "*.rpgc"));
		chooser.setInitialDirectory(new File("characters/"));
		File selectedFile = chooser.showOpenDialog(mainStage);
		if (selectedFile != null) {
			CharacterSheet sheet = CharacterIO.loadCharacter(selectedFile.getAbsolutePath());
			setCharacterSheet(sheet);
			if(sheet != null)
				return true;
		}
		
		return false;
	}
	
	public void setCharacterSheet(CharacterSheet sheet) {
		this.sheet = sheet;
	}
	
	public void loadCharacterPage() {
		try {
			csView.next(new Page((new File("bundles/dnd5e/ui/html/characterSheet.html").toURI().toURL())));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public String getData(String id) {
		Entry data = dataResolver(id, sheet);
		if(data != null) {
			return data.val.toString(); // Only seems to work if string concatentation or the toString() method?
		}
		
		return "";
	}
	
	private Entry dataResolver(String id, Environment env) {
		// Get first id in ID chain
		int dotIdx = id.indexOf('.');
		String subID = null;
		String rem = null;
		if(dotIdx > 0) {
			subID = id.substring(0, dotIdx);
			rem = id.substring(dotIdx + 1);
		}else {
			subID = id;
		}
		
		if(subID != null) {
			try {
				Entry data = env.envGet(subID, -1);
				
				switch(data.type) {
					case BOOLEAN:
					case DOUBLE:
					case INTEGER:
					case STRING:
						return data;
					case ENV:
						return dataResolver(rem, (Environment) data.val);
					case SCRIPT:
						throw new ExistenceException(-1);
					default:
						return null; // unreachable
				}
			} catch (InterpreterRuntimeException e) {
				LOG.error(e.getMessage());
			}
		}
		
		return null;
	}
	
	/**
	 * Echo method for testing Javascript to Java callbacks
	 * @param txt - A message to send
	 * @return The input message
	 */
	public String echo(String txt) {
		LOG.debug(txt);
		return txt;
	}

}
