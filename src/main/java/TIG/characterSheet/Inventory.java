package TIG.characterSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import TIG.features.Features;
import TIG.items.Item;

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
	private final Features features;
	
	public Inventory() {
		this.items = new ArrayList<Item>();
		this.features = new Features();
	}
	
	public Inventory(Item[] items, Features feats) {
		if(items != null)
			this.items = Arrays.asList(items);
		else
			this.items = new ArrayList<Item>();
		
		if(feats != null)
			this.features = feats;
		else
			this.features = new Features();
	}
	
	/**
	 * Used to determine if a item is in the list of items
	 * @param itemUUID - The UUID of the item being looked for
	 * @return Returns true if there is at least one instance of an item with the same item UUID in the list of items
	 */
	public boolean hasItem(UUID itemUUID) {
		for(Item i : this.items) {
			if(i.getUUID().equals(itemUUID)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean addItem(Item item) {
		return items.add(item);
	}
	
	public boolean removeItem(Item item) {
		return items.remove(item);
	}

	public Item[] getItems() {
		return (Item[]) this.items.toArray();
	}
	
	public Features getFeatures() {
		return features;
	}

}