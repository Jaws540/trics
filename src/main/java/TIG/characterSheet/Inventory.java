package TIG.characterSheet;

import TIG.features.Features;
import TIG.items.Items;
import TIG.scripts.Def;
import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.ExistenceException;
import TIG.scripts.compiler.exceptions.InterpreterRuntimeException;
import TIG.utils.Log;

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
	public Entry envGet(String identifier) throws InterpreterRuntimeException {
		switch(identifier) {
			case Def.ITEMS:
				return new Entry(Entry.Type.ENV, items);
			case Def.FEATURES:
				return new Entry(Entry.Type.ENV, features);
			default:
				// Unknown environment
				Log.error("Unknown Inventory environment.");
				throw new ExistenceException();
		}
	}

	@Override
	public boolean envPut(String identifier, Entry obj) {
		// No on should be able to edit an environment
		return false;
	}

}
