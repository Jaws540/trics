package TIG.scripts;

import TIG.scripts.compiler.exceptions.InterpreterRuntimeException;

/**
 * Interface for interacting with environments.
 * @author Jacob
 *
 */
public interface Environment {
	
	/**
	 * Gets a value from the environment by the given unique string identifier
	 * @param identifier - Unique string
	 * @return An entry with the type and value information of the environment data
	 * @throws InterpreterRuntimeException
	 */
	public Entry envGet(String identifier) throws InterpreterRuntimeException;
	
	/**
	 * Puts a value into an existing environment entry.  This will not create a new entry!
	 * @param identifier - Unique string
	 * @param obj - Data to put into entry
	 * @return <code>true</code> on success
	 * @throws InterpreterRuntimeException
	 */
	public boolean envPut(String identifier, Entry obj) throws InterpreterRuntimeException;
	
}
