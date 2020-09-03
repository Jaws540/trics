package TIG.scripts;

import TIG.scripts.compiler.exceptions.interpreterExceptions.InterpreterRuntimeException;

/**
 * Interface for interacting with environments.
 * @author Jacob
 *
 */
public interface Environment {
	
	/**
	 * Gets a value from the environment by the given unique string identifier
	 * @param identifier - Unique string
	 * @param pos - Current offset in the script source string for error reporting.  Set to 0 if not running a script.
	 * @return An entry with the type and value information of the environment data
	 * @throws InterpreterRuntimeException
	 */
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException;
	
	/**
	 * Puts a value into an existing environment entry.  This will not create a new entry!
	 * @param identifier - Unique string
	 * @param obj - Data to put into entry
	 * @param pos - Current offset in the script source string for error reporting.  Set to 0 if not running a script.
	 * @return <code>true</code> on success
	 * @throws InterpreterRuntimeException
	 */
	public boolean envPut(String identifier, Entry obj, int pos) throws InterpreterRuntimeException;
	
}
