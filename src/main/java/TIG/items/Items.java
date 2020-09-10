package TIG.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.utils.exceptions.interpreterExceptions.ExistenceException;
import TIG.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;

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
	public boolean hasItem(String id) {
		for(Item i : this.items) {
			if(i.getID().equals(id)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean addItem(Item item) {
		if(!hasItem(item.getID()))
			return items.add(item);
		
		return false;
	}
	
	public boolean removeItem(Item item) {
		return items.remove(item);
	}

	public Item[] getItems() {
		return this.items.toArray(new Item[this.items.size()]);
	}
	
	public Item getItem(String id) {
		for(Item i : this.items) {
			if(i.getID().equalsIgnoreCase(id)) {
				return i;
			}
		}
		
		return null;
	}

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		Item item = getItem(identifier);
		if(item != null)
			return new Entry(Entry.Type.ENV, item, false);
		
		throw new ExistenceException(pos);
	}

}
