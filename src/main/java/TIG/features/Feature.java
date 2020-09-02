package TIG.features;

import TIG.scripts.Scripts;
import TIG.tags.Tag;
import TIG.tags.Taggable;

/**
 * The main unit for values a character has
 * @author Jacob
 *
 */
public class Feature extends Taggable {
	
	private final String id;
	private final String displayName;
	private final String description;
	
	private final Fields fields;
	
	private final Scripts scripts;
	
	public Feature(String id, String displayName, String desc, Fields fields, Scripts scripts) {
		super();
		this.id = id;
		this.displayName = displayName;
		this.description = desc;
		
		if(fields != null)
			this.fields = fields;
		else
			this.fields = new Fields();
		
		if(scripts != null)
			this.scripts = scripts;
		else
			this.scripts = new Scripts();
	}
	
	/**
	 * Creates a new feature
	 * @param id - Name of the feature
	 * @param displayName - Name of this feature to show to the user
	 * @param desc - Description of the feature and what it does
	 * @param fields - Information specific to this feature.  Ex: Ki Points
	 * @param scripts - Actions that can be done wither by a trigger or manually by the user
	 */
	public Feature(String id, String displayName, String desc, Fields fields, Scripts scripts, Tag[] tags) {
		super(tags);
		this.id = id;
		this.displayName = displayName;
		this.description = desc;
		
		if(fields != null)
			this.fields = fields;
		else
			this.fields = new Fields();
		
		if(scripts != null)
			this.scripts = scripts;
		else
			this.scripts = new Scripts();
	}
	
	/**
	 * This must be a valid identifier
	 * @return The ID of the feature, for internal/script use only.
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * The name used to display to the end user, not for internal use
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * @return The description of the feature and what it does
	 */
	public String getDescription() {
		return this.description;
	}
	
	public Fields getFields() {
		return this.fields;
	}
	
	public Scripts getScripts() {
		return this.scripts;
	}

}
