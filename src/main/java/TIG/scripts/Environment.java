package TIG.scripts;

import TIG.scripts.compiler.exceptions.InterpreterRuntimeException;

/**
 * A simple wrapper around a hashmap to provide a running environment for scripts
 * @author Jacob
 *
 */
public interface Environment {
	
	/**
	 * Gets a value from the environment by the given unique string identifier
	 * @param identifier - Unique string
	 * @return An entry with the type and value information of the environment data
	 * @throws ExistenceException - When the identifier doesn't exist
	 * @throws TypeException
	 */
	public Entry envGet(String identifier) throws InterpreterRuntimeException;
	
	public boolean envPut(String identifier, Entry obj) throws InterpreterRuntimeException;
	
}
