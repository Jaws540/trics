package TIG.scripts;

import TIG.scripts.compiler.Interpretable;
import TIG.utils.Def;
import TIG.utils.Utils;
import TIG.utils.exceptions.interpreterExceptions.ExistenceException;
import TIG.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;
import TIG.utils.io.ScriptIO;

/**
 * Source code that can be run by the user or system.
 * @author Jacob
 *
 */
public class Script extends Interpretable implements Environment {
	/**
	 * String unique identifier
	 */
	private final String id;
	/**
	 * File path to the raw source code of the script
	 */
	private final String sourcePath;
	
	private final String displayName;
	private final String description;
	
	
	public Script(String id, String sourcePath, String displayName, String description) {
		this.id = Utils.validateID(id);
		this.sourcePath = sourcePath;
		this.displayName = displayName;
		this.description = description;
		
		compile(ScriptIO.loadScript(sourcePath));
	}
	
	/**
	 * @return the identifier for the script
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * @return the raw source code of the script
	 */
	public String getSourcePath() {
		return sourcePath;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		switch(identifier) {
			case Def.DISPLAY_NAME:
				return new Entry(Entry.Type.STRING, displayName, false);
			case Def.DESCRIPTION:
				return new Entry(Entry.Type.STRING, description, false);
			default:
				throw new ExistenceException(pos);
		}
	}
	
}
