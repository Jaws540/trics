package TIG.features;

/**
 * Base unit of information
 * @author Jacob
 *
 */
public class Field<T> {
	
	private T value;
	private final String name;
	
	/**
	 * Create a Field that contains a single piece of information
	 * @param id - Name of the field
	 * @param value - Value the field will contain
	 */
	public Field(String id, T value) {
		this.name = id;
		this.value = value;
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
	
}