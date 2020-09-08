package TIG.features;

import TIG.scripts.Entry;
import TIG.scripts.Entry.Type;
import TIG.utils.Utils;

/**
 * Base unit of information
 * @author Jacob
 *
 */
public class Field {
	
	private final String id;
	private final Entry data;
	
	/**
	 * Create a Field that contains a single piece of information
	 * @param id - Name of the field
	 * @param value - Value the field will contain
	 */
	public Field(String id, Entry data) {
		this.id = Utils.validateID(id);
		this.data = data;
	}
	
	/**
	 * Gets the name of this field
	 * @return
	 */
	public String getIdentifier() {
		return this.id;
	}
	
	/**
	 * Gets the Entry data for this field
	 * @return
	 */
	public Entry getData() {
		return this.data;
	}
	
	public Type getType() {
		return data.type;
	}
	
}