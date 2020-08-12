package TIG.scripts;

import TIG.utils.JSONUtils;
import TIG.utils.JSONify;

/**
 * Source code that can be run by the user or system.
 * @author Jacob
 *
 */
public class Script implements JSONify {
	/**
	 * String name of the script
	 */
	private final String identifier;
	/**
	 * File path to the raw source code of the script
	 */
	private final String sourcePath;
	
	private final String description;
	
	public Script(String id, String sourcePath, String description) {
		this.identifier = id;
		this.sourcePath = sourcePath;
		this.description = description;
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

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Name", this.identifier));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Description", this.description));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Source Path", this.sourcePath));
		output.append("\n");
		output.append(indentString.substring(0, indentString.length() - 1));
		output.append("}");
		
		return output.toString();
	}
}
