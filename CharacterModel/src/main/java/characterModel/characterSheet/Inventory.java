package characterModel.characterSheet;

import characterModel.features.Features;
import characterModel.items.Items;
import characterModel.scripts.Entry;
import characterModel.scripts.Environment;
import characterModel.utils.Def;
import characterModel.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;

public class Inventory implements Environment {
	
	private final Items items;
	
	/*
	 * Reserved Features:
	 * 		- Item: This should be explicitly avoided as a feat name as it refers to the item list not a field
	 * 
	 * (I don't know about these \/ )
	 * 		- carry_capacity: The maximum weight able to be carried
	 * 		- carry_weight: The current weight of all items in this inventory
	 */
	private final Features features;
	
	public Inventory() {
		this.items = new Items();
		this.features = new Features();
	}
	
	public Inventory(Items items, Features feats) {
		if(items != null)
			this.items = items;
		else
			this.items = new Items();
		
		if(feats != null)
			this.features = feats;
		else
			this.features = new Features();
	}
	
	public Items getItems() {
		return items;
	}
	
	public Features getFeatures() {
		return features;
	}

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		switch(identifier) {
			case Def.FEATURES:
				return new Entry(Entry.Type.ENV, features, false);
			default:
				return items.envGet(identifier, pos);
		}
	}

}
