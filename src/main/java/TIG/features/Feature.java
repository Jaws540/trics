package TIG.features;

import TIG.scripts.Scripts;
import TIG.tags.Tag;
import TIG.tags.Taggable;
import TIG.utils.JSONUtils;
import TIG.utils.JSONify;

/**
 * The main unit for values a character has
 * @author Jacob
 *
 */
public class Feature extends Taggable implements JSONify {
	
	private final String name;
	private final String description;
	
	private final Fields fields;
	
	private final Scripts scripts;
	
	public Feature(String name, String desc, Fields fields, Scripts scripts) {
		super();
		this.name = name;
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
	 * @param id - A randomly generated UUID specific to this feature (not this instance!)
	 * @param name - Name of the feature
	 * @param desc - Description of the feature and what it does
	 * @param fields - Information specific to this feature.  Ex: Ki Points
	 * @param scripts - Actions that can be done wither by a trigger or manually by the user
	 */
	public Feature(String name, String desc, Fields fields, Scripts scripts, Tag[] tags) {
		super(tags);
		this.name = name;
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
	
	public Fields getFields() {
		return this.fields;
	}
	
	public Scripts getScripts() {
		return this.scripts;
	}

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Name", getName()));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Description", getDescription()));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Fields", this.fields.toJSON(indent + 1)));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Scripts", this.scripts.toJSON(indent + 1)));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Tags", super.toJSON(indent + 1)));
		output.append("\n");
		output.append(indentString.substring(0, indentString.length() - 1));
		output.append("}");
		
		return output.toString();
	}

}
