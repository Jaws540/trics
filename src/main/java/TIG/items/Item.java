package TIG.items;

import TIG.features.Feature;
import TIG.features.Features;
import TIG.features.Field;
import TIG.features.Fields;
import TIG.scripts.Def;
import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.interpreterExceptions.ImmutableException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.InterpreterRuntimeException;
import TIG.tags.Tag;
import TIG.tags.Taggable;
import TIG.utils.Log;
import TIG.utils.Utils;

public class Item extends Taggable implements Environment {
	
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
	
	private final String id;
	private final String displayName;
	private final String description;
	
	/*
	 * Each item will have a standard 'std::item' feature.
	 * This feat will contain the the reserved fields weight and value (not equipped!).
	 * This will not contain the reserved scripts.  The reserved scripts must
	 * be added into a separate feature.
	 */
	private final Features feats;
	
	public Item(String id, String displayName, String desc, double weight, double value, int count, Features feats) {
		super();
		this.id = Utils.validateID(id);
		this.displayName = displayName;
		this.description = desc;
		
		if(feats != null)
			this.feats = feats;
		else
			this.feats = new Features();
		
		addBaseFeature(weight, value, count);
	}
	
	/**
	 * Creates an Item
	 * @param name - Name for the item
	 * @param desc - Description of the item
	 * @param fields - Any additional data needed for the item (Check the list of reserved fields that can be added)
	 * @param scripts - Any actions that can be taken with this item (Check the list of reserved scripts that can be added)
	 * @param weight - The item's in-game weight
	 * @param value - The item's in-game value
	 */
	public Item(String id, String displayName, String desc, double weight, double value, int count, Features feats, Tag[] tags) {
		super(tags);
		this.id = Utils.validateID(id);
		this.displayName = displayName;
		this.description = desc;
		
		if(feats != null)
			this.feats = feats;
		else
			this.feats = new Features();
		
		addBaseFeature(weight, value, count);
	}
	
	private void addBaseFeature(double weight, double value, int itemCount) {
		Field<?>[] baseFieldList = null;
		try {
			Field<?>[] tmp = {
					new Field<Double>("weight", weight, Field.Type.DOUBLE), 
					new Field<Double>("value", value, Field.Type.DOUBLE),
					new Field<Integer>("count", itemCount, Field.Type.INT)
				  };
			baseFieldList = tmp;
		} catch(Exception e) {
			Log.warn("Failed to initialize std::item Feature.  ID: " + id);
			return;
		};
		Fields baseFields = new Fields(baseFieldList);
		Feature base = new Feature("std::item", "Item", "Basic attributes for all items", baseFields, null, null);
		this.feats.addFeature(base);
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Features getFeatures() {
		return feats;
	}

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		switch(identifier) {
			case Def.DISPLAY_NAME:
				return new Entry(Entry.Type.STRING, displayName);
			case Def.DESCRIPTION:
				return new Entry(Entry.Type.STRING, description);
			default:
				return feats.envGet(identifier, pos);
		}
	}

	@Override
	public boolean envPut(String identifier, Entry obj, int pos) throws InterpreterRuntimeException {
		switch(identifier) {
		case Def.DISPLAY_NAME:
		case Def.DESCRIPTION:
			throw new ImmutableException(pos);
		default:
			return feats.envPut(identifier, obj, pos);
		}
	}

}
