package TIG.features;

import java.util.regex.Pattern;

import TIG.scripts.Entry;

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
	private final String name;
	private final Type type;
	
	private final transient String idRegex = "^[A-Za-z_][A-Za-z0-9_]*$";
	private final transient Pattern idPatt = Pattern.compile(idRegex);
	
	/**
	 * Create a Field that contains a single piece of information
	 * @param id - Name of the field
	 * @param value - Value the field will contain
	 */
	public Field(String id, T value, Type type) throws Exception {
		if(idPatt.matcher(id).matches()) {
			this.name = id;
		}else {
			throw new Exception("Invalid field ID.");
		}
		this.value = value;
		this.type = type;
	}
	
	/**
	 * Gets the name of this field
	 * @return
	 */
	public String getIdentifier() {
		return this.name;
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