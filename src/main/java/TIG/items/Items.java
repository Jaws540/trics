package TIG.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import TIG.scripts.Entry;
import TIG.scripts.Environment;

public class Items implements Environment {
	
	private final List<Item> items;
	
	public Items() {
		this.items = new ArrayList<>();
	}
	
	public Items(Item[] items) {
		if(items != null)
			this.items = Arrays.asList(items);
		else
			this.items = new ArrayList<Item>();
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

	@Override
	public Entry envGet(String identifier) {
		return null;
	}

	@Override
	public boolean envPut(String identifier, Entry obj) {
		return false;
	}

}
