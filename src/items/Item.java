package items;

import java.util.UUID;

import characterSheet.features.Feature;
import characterSheet.features.Features;
import characterSheet.features.Field;
import tags.Tag;
import tags.Taggable;

public class Item extends Taggable {
	
	/*
	 * std::item Feature Fields:
	 * 		- weight: Carry weight for one of this item
	 * 		- value: The coin value of this item
	 * 		- stackable: If an item is stackable, it will not be modifiable.  This is so items can be edited but have the same name.
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
	
	// Alpha-numeric unique identifier for this specific item
	private final UUID id;
	
	/*
	 * Each item will have a standard 'std::item' feature.
	 * This feat will contain the the reserved fields weight and value (not equipped!).
	 * This will not contain the reserved scripts.  The reserved scripts must
	 * be added into a separate feature.
	 */
	private final Features feats;

	/**
	 * Creates an Item
	 * @param name - Name for the item
	 * @param desc - Description of the item
	 * @param fields - Any additional data needed for the item (Check the list of reserved fields that can be added)
	 * @param scripts - Any actions that can be taken with this item (Check the list of reserved scripts that can be added)
	 * @param weight - The item's in-game weight
	 * @param value - The item's in-game value
	 * @param id - a randomly generated UUID that is specific to each item (not each instance!)
	 */
	public Item(String name, String desc, double weight, double value, Feature[] feats, Tag[] tags, UUID id) {
		super(tags);
		this.name = name;
		this.description = desc;
		this.feats = new Features(feats);
		this.id = id;
		
		Field<?>[] baseFields = {new Field<Double>("weight", weight), new Field<Double>("value", value)};
		Feature base = new Feature("std::item", "Basic attributes for all items", baseFields, null, null, false);
		this.feats.addFeature(base);
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
	public Item(String name, String desc, double weight, double value, Feature[] feats, Tag[] tags, UUID id, boolean stdItemInfoSecret) {
		this(name, desc, weight, value, feats, tags, id);
		
		Field<?>[] baseFields = {new Field<Double>("weight", weight), new Field<Double>("value", value)};
		Feature base = new Feature("std::item", "Basic attributes for all items", baseFields, null, null, stdItemInfoSecret);
		this.feats.addFeature(base);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Features getFeatures() {
		return feats;
	}

	public UUID getId() {
		return id;
	}

}
