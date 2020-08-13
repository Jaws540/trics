package TIG.scripts;

/**
 * Source code that can be run by the user or system.
 * @author Jacob
 *
 */
public class Script {
	/**
	 * String name of the script
	 */
	private final String name;
	/**
	 * File path to the raw source code of the script
	 */
	private final String sourcePath;
	
	private final String description;
	
	public Script(String name, String sourcePath, String description) {
		this.name = name;
		this.sourcePath = sourcePath;
		this.description = description;
	}
	
	/**
	 * @return the name of the script
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the raw source code of the script
	 */
	public String getSourcePath() {
		return this.sourcePath;
	}
	
	/**
	 * Runs this script given an environment
	 * @param env
	 */
	public void run(Environment env) {
		System.out.println("TODO: Run scripts within an environment");
	}

	public String getDescription() {
		return description;
	}
	
}
