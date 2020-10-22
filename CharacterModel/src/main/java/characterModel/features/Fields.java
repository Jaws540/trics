package characterModel.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import characterModel.scripts.Entry;
import characterModel.scripts.Environment;
import characterModel.utils.exceptions.interpreterExceptions.ExistenceException;
import characterModel.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;

public class Fields implements Environment {
	
	private final List<Field> fields;
	
	public Fields() {
		this.fields = new ArrayList<Field>();
	}
	
	public Fields(Field[] fields) {
		if(fields != null)
			this.fields = Arrays.asList(fields);
		else
			this.fields = new ArrayList<Field>();
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
	public Field getField(String identifier) {
		for(Field f : this.fields) {
			if(f.getIdentifier().equals(identifier))
				return f;
		}
		return null;
	}

	public Field[] getFields() {
		return this.fields.toArray(new Field[this.fields.size()]);
	}

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		Field f = getField(identifier);
		if(f != null) {
			return f.getData();
		}
		
		// If no field exists by the given identifier
		throw new ExistenceException(pos);
	}

}
