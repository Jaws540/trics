package TIG.characterSheet;

import TIG.features.Features;
import TIG.notes.Notes;

/**
 * Interface for interaction with character sheets
 * @author Jacob
 *
 */
public class CharacterSheet {
	
	private final Info info;
	private final Features features;
	private final Inventory inventory;
	private final Notes notes;
	
	public CharacterSheet(Info info, Features feats, Inventory inventory, Notes notes) {
		this.info = info;
		this.features = feats;
		this.inventory = inventory;
		this.notes = notes;
	}

	public Info getGeneralInfo() {
		return info;
	}

	public Features getFeatures() {
		return features;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Notes getNotes() {
		return notes;
	}

}
