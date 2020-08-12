package TIG.characterSheet;

import TIG.features.Features;
import TIG.notes.Notes;
import TIG.utils.JSONUtils;
import TIG.utils.JSONify;

/**
 * Interface for interaction with character sheets
 * @author Jacob
 *
 */
public class CharacterSheet implements JSONify {
	
	private final Info generalInfo;
	private final Features feats;
	private final Inventory inventory;
	private final Notes notes;
	
	public CharacterSheet(Info info, Features feats, Inventory inventory, Notes notes) {
		this.generalInfo = info;
		this.feats = feats;
		this.inventory = inventory;
		this.notes = notes;
	}

	public Info getGeneralInfo() {
		return generalInfo;
	}

	public Features getFeatures() {
		return feats;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Notes getNotes() {
		return notes;
	}

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Info", this.generalInfo.toJSON(indent + 1)));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Features", this.feats.toJSON(indent + 1)));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Inventory", this.inventory.toJSON(indent + 1)));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Notes", this.notes.toJSON(indent + 1)));
		output.append("\n");
		output.append("}");
		
		return output.toString();
	}

}
