package TIG.scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scripts {
	
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
	
	public Scripts[] getScripts() {
		return this.scripts.toArray(new Scripts[this.scripts.size()]);
	}
	
	/**
	 * Gets the names of all the Scripts defined for this feature
	 * @return A String array of script names
	 */
	public String[] getScriptNames() {
		String[] names = new String[this.scripts.size()];
		for(int i = 0; i < this.scripts.size(); i++) {
			names[i] = this.scripts.get(i).getName();
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
			if(s.getName().equals(identifier))
				return s;
		}
		return null;
	}

}
