package characterSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import features.Features;
import items.Item;
import utils.JSONUtils;
import utils.JSONify;

public class Inventory implements JSONify {
	
	private final List<Item> items;
	
	/*
	 * Reserved Features:
	 * 		- Item: This should be explicitly avoided as a feat name as it refers to the item list not a field
	 * 
	 * (I don't know about these \/ )
	 * 		- carry_capacity: The maximum weight able to be carried
	 * 		- carry_weight: The current weight of all items in this inventory
	 */
	private final Features feats;
	
	public Inventory() {
		this.items = new ArrayList<Item>();
		this.feats = new Features();
	}
	
	public Inventory(Item[] items, Features feats) {
		if(items != null)
			this.items = Arrays.asList(items);
		else
			this.items = new ArrayList<Item>();
		
		if(feats != null)
			this.feats = feats;
		else
			this.feats = new Features();
	}
	
	/**
	 * Used to determine if a item is in the list of items
	 * @param itemUUID - The UUID of the item being looked for
	 * @return Returns true if there is at least one instance of an item with the same item UUID in the list of items
	 */
	public boolean hasItem(UUID itemUUID) {
		for(Item i : this.items) {
			if(i.getId().equals(itemUUID)) {
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
		return feats;
	}

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{\n");
		output.append(indentString);
		output.append("\"Items\": [\n");
		String indentString2 = JSONUtils.getIndent(indent + 1);
		for(Item i : this.items) {
			output.append(indentString2);
			output.append(i.toJSON(indent + 2));
			output.append(",\n");
		}
		if(this.items.size() > 0) {
			// Remove trailing comma
			output.deleteCharAt(output.length() - 2);
			output.append(indentString);
		}else {
			output.deleteCharAt(output.length() - 1);
		}
		output.append("],\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Features", this.feats.toJSON(indent + 1)));
		output.append("\n");
		output.append(indentString.substring(0, indentString.length() - 1));
		output.append("}");
		
		return output.toString();
	}

}
