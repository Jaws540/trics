package items;

import java.util.Arrays;
import java.util.List;

import CharacterSheetAPI.Feature;
import CharacterSheetAPI.Field;

public class Item {
	
	/*
	 * std::item Feature Fields:
	 * 		- weight: Carry weight for one of this item
	 * 		- value: The coin value of this item
	 * 
	 * Reserved item features
	 * 		- std::equippable: 
	 * 			- Fields:
	 * 				- equipped: boolean flag denoting if the item is equipped
	 * 			- Scripts:
	 * 				- onEquip: Runs when the equipped flag goes from false -> true
	 * 				- onDequip: Runs when the equipped flag goes from true -> false
	 * 		- std::usable:
	 * 			- Fields:
	 * 			- Scripts:
	 * 				- onUse: Runs when the item is used (Can either be used as a consumable or as a weapon, possibly other cases as well).
	 * 		- std::add_remove
	 * 			- Fields:
	 * 			- Scripts:
	 * 				- onPickup: Runs when the item is added to an inventory
	 * 				- onDrop: Runs when the item is removed from an inventory
	 */
	
	private final String name;
	private final String description;
	
	/*
	 * Each item will have a standard 'std::item' feature.
	 * This feat will contain the the reserved fields weight and value (not equipped!).
	 * This will not contain the reserved scripts.  The reserved scripts must
	 * be added into a separate feature.
	 */
	private final List<Feature> feats;

	/**
	 * Creates an Item
	 * @param name - Name for the item
	 * @param desc - Description of the item
	 * @param fields - Any additional data needed for the item (Check the list of reserved fields that can be added)
	 * @param scripts - Any actions that can be taken with this item (Check the list of reserved scripts that can be added)
	 * @param weight - The item's in-game weight
	 * @param value - The item's in-game value
	 */
	public Item(String name, String desc, Feature[] feats, double weight, double value) {
		this.name = name;
		this.description = desc;
		this.feats = Arrays.asList(feats);
		
		Field<?>[] baseFields = {new Field<Double>("weight", weight), new Field<Double>("value", value)};
		Feature base = new Feature("std::item", "Basic attributes for all items", baseFields, null, false);
		this.feats.add(base);
	}
	
	/**
	 * Creates an Item
	 * @param name - Name for the item
	 * @param desc - Description of the item
	 * @param fields - Any additional data needed for the item (Check the list of reserved fields that can be added)
	 * @param scripts - Any actions that can be taken with this item (Check the list of reserved scripts that can be added)
	 * @param weight - The item's in-game weight
	 * @param value - The item's in-game value
	 * @param stdItemInfoSecret - Set to true to hide the weight and value of the item
	 */
	public Item(String name, String desc, Feature[] feats, double weight, double value, boolean stdItemInfoSecret) {
		this.name = name;
		this.description = desc;
		this.feats = Arrays.asList(feats);
		
		Field<?>[] baseFields = {new Field<Double>("weight", weight), new Field<Double>("value", value)};
		Feature base = new Feature("std::item", "Basic attributes for all items", baseFields, null, stdItemInfoSecret);
		this.feats.add(base);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Feature[] getFeatures() {
		return (Feature[]) this.feats.toArray();
	}

}
