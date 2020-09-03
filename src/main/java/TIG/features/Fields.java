package TIG.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.interpreterExceptions.ExistenceException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.InterpreterRuntimeException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.TypeException;

public class Fields implements Environment {
	
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

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		Field<?> f = getField(identifier);
		if(f != null) {
			switch(f.getType()) {
				case BOOL:
					return new Entry(Entry.Type.BOOL, (Boolean) f.getValue());
				case DOUBLE:
					return new Entry(Entry.Type.DOUBLE, (Double) f.getValue());
				case INT:
					return new Entry(Entry.Type.INT, ((Double) f.getValue()).intValue());
				case STRING:
					return new Entry(Entry.Type.STRING, (String) f.getValue());
				default:
					throw new TypeException(pos, "Unknown type.");
			}
		}
		
		// If no field exists by the given identifier
		throw new ExistenceException(pos);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean envPut(String identifier, Entry obj, int pos) throws InterpreterRuntimeException {
		// If it exists, get the field identified
		Field<?> f = getField(identifier);
		if(f != null) {
			// If the type of the field matches the type of the assignment entry
			if(f.getType().isEntryType(obj.type)) {
				// Set
				switch(f.getType()) {
					case BOOL:
						((Field<Boolean>) f).setValue((Boolean) obj.val);
						return true;
					case DOUBLE:
						((Field<Double>) f).setValue((Double) obj.val);
						return true;
					case INT:
						((Field<Integer>) f).setValue((Integer) obj.val);
						return true;
					case STRING:
						((Field<String>) f).setValue((String) obj.val);
						return true;
					default:
						throw new TypeException(pos, "Unknown type.");
				}
			}else {
				throw new TypeException(pos, "Expected " + f.getType().name);
			}
		}
		
		// If the field doesn't exist
		throw new ExistenceException(pos);
	}

}
