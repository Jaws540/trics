package characterSheet.features;

import java.util.Arrays;
import java.util.List;

import characterSheet.scripts.Script;
import tags.Tag;
import tags.Taggable;

/**
 * The main unit for values a character has
 * @author Jacob
 *
 */
public class Feature extends Taggable {
	
	private final String name;
	private final String description;
	
	private final List<Field<?>> fields;
	
	private final List<Script> scripts;
	
	private boolean secret;
	
	/**
	 * Creates a new feature
	 * @param name - Name of the feature
	 * @param desc - Description of the feature and what it does
	 * @param fields - Information specific to this feature.  Ex: Ki Points
	 * @param scripts - Actions that can be done wither by a trigger or manually by the user
	 */
	public Feature(String name, String desc, Field<?>[] fields, Script[] scripts, Tag[] tags, boolean secret) {
		super(tags);
		this.name = name;
		this.description = desc;
		this.fields = Arrays.asList(fields);
		this.scripts = Arrays.asList(scripts);
		this.secret = secret;
	}
	
	/**
	 * @return The name of the feature
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The description of the feature and what it does
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Gets the names of all the Fields specifically defined for the feature
	 * @return A String array of names
	 */
	public String[] getFieldNames() {
		String[] names = new String[this.fields.size()];
		for(int i = 0; i < this.fields.size(); i++) {
			names[i] = this.fields.get(i).getIdentifier();
		}
		return names;
	}
	
	/**
	 * Gets a specific Field object
	 * @param identifier - The name of the Field
	 * @return The Field object (Can be edited)
	 */
	public Field<?> getField(String identifier) {
		for(Field<?> f : this.fields) {
			if(f.getIdentifier().equals(identifier))
				return f;
		}
		return null;
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
	
	/**
	 * Used to determine if the description, fields and scripts of this feature are secret
	 * @return
	 */
	public boolean isSecret() {
		return this.secret;
	}
	
	public void setSecret(boolean secret) {
		this.secret = secret;
	}

}
