package CharacterSheetAPI.scripts;

/**
 * Source code that can be run by the user or system.
 * @author Jacob
 *
 */
public class Script {
	/**
	 * String name of the script
	 */
	private final String identifier;
	/**
	 * Raw source code of the script
	 */
	private final String source;
	
	public Script(String id, String source) {
		this.identifier = id;
		this.source = source;
	}
	
	/**
	 * @return the name of the script
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * @return the raw source code of the script
	 */
	public String getSource() {
		return this.source;
	}
	
	/**
	 * Runs this script given an environment
	 * @param env
	 */
	public void run(Environment env) {
		System.out.println("TODO: Run scripts within an environment");
	}
}
