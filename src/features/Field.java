package features;

import utils.JSONUtils;
import utils.JSONify;

/**
 * Base unit of information
 * @author Jacob
 *
 */
public class Field<T> implements JSONify {
	private T value;
	private final String identifier;
	
	/**
	 * Create a Field that contains a single piece of information
	 * @param id - Name of the field
	 * @param value - Value the field will contain
	 */
	public Field(String id, T value) {
		this.identifier = id;
		this.value = value;
	}
	
	/**
	 * Gets the name of this field
	 * @return
	 */
	public String getIdentifier() {
		return this.identifier;
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

	@Override
	public String toJSON(int indent) {
		/*
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Name", this.identifier));
		output.append(",");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Value", this.value.toJSON(indent + 1)));
		output.append("}");
					
		return output.toString();
		* 
		* Deciding to use the below code instead for now...
		*/
		
		if(this.value instanceof JSONify) {
			JSONify val = (JSONify) this.value;
			return JSONUtils.basicJSONifyJSON(this.identifier, val.toJSON(indent + 1));
		}else {
			return JSONUtils.basicJSONify(this.identifier, this.value.toString());
		}
	}
}