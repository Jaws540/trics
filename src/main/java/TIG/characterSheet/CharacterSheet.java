package TIG.characterSheet;

import TIG.features.Features;
import TIG.notes.Notes;
import TIG.scripts.Def;
import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.ExistenceException;
import TIG.scripts.compiler.exceptions.ImmutableException;
import TIG.scripts.compiler.exceptions.InterpreterRuntimeException;
import TIG.utils.Log;

/**
 * Interface for interaction with character sheets
 * @author Jacob
 *
 */
public class CharacterSheet implements Environment {
	
	private final Info info;
	private final Features features;
	private final Inventory inventory;
	private final Notes notes;
	
	public CharacterSheet(Info info, Features feats, Inventory inventory, Notes notes) {
		this.info = info;
		this.features = feats;
		this.inventory = inventory;
		this.notes = notes;
	}

	public Info getGeneralInfo() {
		return info;
	}

	public Features getFeatures() {
		return features;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Notes getNotes() {
		return notes;
	}

	@Override
	public Entry envGet(String identifier) throws InterpreterRuntimeException {
		switch(identifier) {
			case Def.INFO:
				return new Entry(Entry.Type.ENV, info);
			case Def.FEATURES:
				return new Entry(Entry.Type.ENV, features);
			case Def.INVENTORY:
				return new Entry(Entry.Type.ENV, inventory);
			default:
				Log.error("Unknown character environment.");
				throw new ExistenceException();
		}
	}

	@Override
	public boolean envPut(String identifier, Entry obj) throws InterpreterRuntimeException {
		throw new ImmutableException();
	}

}
