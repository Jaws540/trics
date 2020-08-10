package CharacterSheetAPI;

import java.util.Arrays;
import java.util.List;

import items.Item;

public class Inventory {
	
	private final List<Item> items;
	
	/*
	 * Reserved Features:
	 * 		- Item: This should be explicitly avoided as a feat name as it refers to the item list not a field
	 * 
	 * (I don't know about these \/ )
	 * 		- carry_capacity: The maximum weight able to be carried
	 * 		- carry_weight: The current weight of all items in this inventory
	 */
	private final List<Feature> feats;
	
	public Inventory(Item[] items, Feature[] feats) {
		this.items = Arrays.asList(items);
		this.feats = Arrays.asList(feats);
	}

	public List<Item> getItems() {
		return items;
	}

	public List<Feature> getFeats() {
		return feats;
	}

}
