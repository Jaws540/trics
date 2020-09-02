package TIG.features;

import TIG.scripts.Entry;
import TIG.utils.Utils;

/**
 * Base unit of information
 * @author Jacob
 *
 */
public class Field<T> {
	
	public enum Type {
		INT,
		DOUBLE,
		BOOL,
		STRING;
		
		public boolean isEntryType(Entry.Type type) {
			switch(type) {
				case BOOL:
					if(this == Type.BOOL)
						return true;
				case DOUBLE:
					if(this == Type.DOUBLE)
						return true;
				case INT:
					if(this == Type.INT)
						return true;
				case STRING:
					if(this == Type.STRING)
						return true;
				default:
					return false;
			}
		}
	}
	
	private T value;
	private final String id;
	private final Type type;
	
	/**
	 * Create a Field that contains a single piece of information
	 * @param id - Name of the field
	 * @param value - Value the field will contain
	 */
	public Field(String id, T value, Type type) throws Exception {
		this.id = Utils.validateID(id);
		this.value = value;
		this.type = type;
	}
	
	/**
	 * Gets the name of this field
	 * @return
	 */
	public String getIdentifier() {
		return this.id;
	}
	
	/**
	 * Gets the value of this field
	 * @return
	 */
	public T getValue() {
		return this.value;
	}
	
	/**
	 * Sets a new value for this field
	 * @param newValue
	 * @return The old value of this field
	 */
	public T setValue(T newValue) {
		T oldValue = this.value;
		this.value  = newValue;
		return oldValue;
	}
	
	public Type getType() {
		return type;
	}
	
}