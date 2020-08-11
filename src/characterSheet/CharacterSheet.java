package characterSheet;

import java.util.Arrays;
import java.util.List;

import characterSheet.features.Feature;
import characterSheet.notes.Notes;

/**
 * Interface for interaction with character sheets
 * @author Jacob
 *
 */
public class CharacterSheet {
	
	private final Info generalInfo;
	private final List<Feature> features;
	private final Inventory inventory;
	private final Notes notes;
	
	public CharacterSheet(Info info, Feature[] features, Inventory inventory, Notes notes) {
		this.generalInfo = info;
		this.features = Arrays.asList(features);
		this.inventory = inventory;
		this.notes = notes;
	}

	public Info getGeneralInfo() {
		return generalInfo;
	}

	/**
	 * Gets the names of all the Fields specifically defined for the feature
	 * @return A String array of names
	 */
	public String[] getFeatureNames() {
		String[] names = new String[this.features.size()];
		for(int i = 0; i < this.features.size(); i++) {
			names[i] = this.features.get(i).getName();
		}
		return names;
	}
	
	/**
	 * Gets a specific Field object
	 * @param identifier - The name of the Field
	 * @return The Field object (Can be edited)
	 */
	public Feature getFeature(String identifier) {
		for(Feature f : this.features) {
			if(f.getName().equals(identifier))
				return f;
		}
		return null;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Notes getNotes() {
		return notes;
	}
	
	public static void main(String[] args) {
		
	}

}
