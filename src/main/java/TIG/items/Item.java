package TIG.items;

import java.util.UUID;

import TIG.features.Feature;
import TIG.features.Features;
import TIG.features.Field;
import TIG.features.Fields;
import TIG.tags.Tag;
import TIG.tags.Taggable;
import TIG.utils.Log;

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
	
	private final UUID uuid;
	
	private final String name;
	private final String description;
	
	/*
	 * Each item will have a standard 'std::item' feature.
	 * This feat will contain the the reserved fields weight and value (not equipped!).
	 * This will not contain the reserved scripts.  The reserved scripts must
	 * be added into a separate feature.
	 */
	private final Features feats;
	
	public Item(UUID id, String name, String desc, double weight, double value, Features feats) {
		super();
		this.uuid = id;
		this.name = name;
		this.description = desc;
		
		if(feats != null)
			this.feats = feats;
		else
			this.feats = new Features();
		
		addBaseFeature(weight, value, false);
	}

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
	public Item(UUID id, String name, String desc, double weight, double value, Features feats, boolean stackable) {
		super();
		this.uuid = id;
		this.name = name;
		this.description = desc;
		
		if(feats != null)
			this.feats = feats;
		else
			this.feats = new Features();
		
		addBaseFeature(weight, value, stackable);
	}
	
	/**
	 * Creates an Item
	 * @param name - Name for the item
	 * @param desc - Description of the item
	 * @param fields - Any additional data needed for the item (Check the list of reserved fields that can be added)
	 * @param scripts - Any actions that can be taken with this item (Check the list of reserved scripts that can be added)
	 * @param weight - The item's in-game weight
	 * @param value - The item's in-game value
	 * @param stackable - Set to true to hide the weight and value of the item
	 */
	public Item(UUID id, String name, String desc, double weight, double value, Features feats, boolean stackable, Tag[] tags) {
		super(tags);
		this.uuid = id;
		this.name = name;
		this.description = desc;
		
		if(feats != null)
			this.feats = feats;
		else
			this.feats = new Features();
		
		addBaseFeature(weight, value, stackable);
	}
	
	private void addBaseFeature(double weight, double value, boolean stackable) {
		Field<?>[] baseFieldList = null;
		try {
			Field<?>[] tmp = {
					new Field<Double>("weight", weight, Field.Type.DOUBLE), 
					new Field<Double>("value", value, Field.Type.DOUBLE),
					new Field<Boolean>("stackable", stackable, Field.Type.BOOL)
				  };
			baseFieldList = tmp;
		} catch(Exception e) {
			Log.warn("Failed to initialize std::item Feature.  UUID: " + this.uuid.toString());
			return;
		};
		Fields baseFields = new Fields(baseFieldList);
		Feature base = new Feature("std::item", "Basic attributes for all items", baseFields, null, null);
		this.feats.addFeature(base);
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSName() {
		return name.replaceAll("[^\\w]", "_");
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Features getFeatures() {
		return feats;
	}

}
