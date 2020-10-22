package characterModel.scripts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import characterModel.utils.exceptions.interpreterExceptions.ExistenceException;
import characterModel.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;

public class Scripts implements Environment {
	
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
	
	public Script[] getScripts() {
		return this.scripts.toArray(new Script[this.scripts.size()]);
	}
	
	/**
	 * Gets the names of all the Scripts defined for this feature
	 * @return A String array of script names
	 */
	public String[] getScriptNames() {
		String[] names = new String[this.scripts.size()];
		for(int i = 0; i < this.scripts.size(); i++) {
			names[i] = this.scripts.get(i).getID();
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
			if(s.getID().equals(identifier))
				return s;
		}
		return null;
	}

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		Script s = getScript(identifier);
		if(s != null) {
			return new Entry(Entry.Type.SCRIPT, s, false);
		}
		
		throw new ExistenceException(pos);
	}

}
