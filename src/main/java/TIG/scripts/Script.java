package TIG.scripts;

import TIG.scripts.compiler.Interpreter;
import TIG.utils.Def;
import TIG.utils.Utils;
import TIG.utils.exceptions.interpreterExceptions.ExistenceException;
import TIG.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;

/**
 * Source code that can be run by the user or system.
 * @author Jacob
 *
 */
public class Script implements Environment {
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
	
	private final Interpreter interp;
	private boolean compiled = false;
	
	public Script(String id, String sourcePath, String displayName, String description) {
		this.id = Utils.validateID(id);
		this.sourcePath = sourcePath;
		this.displayName = displayName;
		this.description = description;
		this.interp = new Interpreter(sourcePath);
		
		compiled = interp.compile();
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
	
	/**
	 * Runs this script given an environment
	 * @param env
	 */
	public void run(Environment env) {
		interp.run(env);
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}
	
	/**
	 * Used to determine if a script compiled correctly or not
	 * @return true if a correct compile, false if the script failed to compile
	 */
	public boolean isCompiled() {
		return compiled;
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
