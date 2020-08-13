package TIG.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fields {
	
	private final List<Field<?>> fields;
	
	public Fields() {
		this.fields = new ArrayList<Field<?>>();
	}
	
	public Fields(Field<?>[] fields) {
		if(fields != null)
			this.fields = Arrays.asList(fields);
		else
			this.fields = new ArrayList<Field<?>>();
	}
	
	/**
	 * Gets the names of all the Fields specifically defined for the feature
	 * @return A String array of names
	 */
	public String[] getFieldNames() {
		String[] names = new String[this.fields.size()];
		for(int i = 0; i < this.fields.size(); i++) {
			names[i] = this.fields.get(i).getIdentifier();
		}
		return names;
	}
	
	/**
	 * Gets a specific Field object
	 * @param identifier - The name of the Field
	 * @return The Field object (Can be edited)
	 */
	public Field<?> getField(String identifier) {
		for(Field<?> f : this.fields) {
			if(f.getIdentifier().equals(identifier))
				return f;
		}
		return null;
	}

	public Field<?>[] getFields() {
		return this.fields.toArray(new Field<?>[this.fields.size()]);
	}

}
