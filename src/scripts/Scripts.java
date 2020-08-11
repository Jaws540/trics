package scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.JSONUtils;
import utils.JSONify;

public class Scripts implements JSONify {
	
	private final List<Script> scripts;
	
	public Scripts() {
		this.scripts = new ArrayList<Script>();
	}
	
	public Scripts(Script[] scripts) {
		if(scripts != null)
			this.scripts = Arrays.asList(scripts);
		else
			this.scripts = new ArrayList<Script>();
	}
	
	/**
	 * Gets the names of all the Scripts defined for this feature
	 * @return A String array of script names
	 */
	public String[] getScriptNames() {
		String[] names = new String[this.scripts.size()];
		for(int i = 0; i < this.scripts.size(); i++) {
			names[i] = this.scripts.get(i).getIdentifier();
		}
		return names;
	}
	
	
	/**
	 * Gets a specific Script object
	 * @param identifier - The name of the Script
	 * @return The Script object that can be run
	 */
	public Script getScript(String identifier) {
		for(Script s : this.scripts) {
			if(s.getIdentifier().equals(identifier))
				return s;
		}
		return null;
	}

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("[\n");
		for(Script s : this.scripts) {
			output.append(indentString);
			output.append(s.toJSON(indent + 1));
			output.append(",\n");
		}
		if(this.scripts.size() > 0) {
			// Remove trailing comma
			output.deleteCharAt(output.length() - 2);
			output.append(indentString.substring(0, indentString.length() - 1));
		}else {
			output.deleteCharAt(output.length() - 1);
		}
		output.append("]");
		
		return output.toString();
	}

}
