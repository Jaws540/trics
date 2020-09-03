package TIG.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.interpreterExceptions.ExistenceException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.ImmutableException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.InterpreterRuntimeException;

public class Features implements Environment {
	
	private final List<Feature> features;
	
	public Features() {
		this.features = new ArrayList<Feature>();
	}
	
	/**
	 * List of Features
	 * @param feats - Feats to be added to this list
	 */
	public Features(Feature[] feats) {
		if(feats != null)
			this.features = Arrays.asList(feats);
		else
			this.features = new ArrayList<Feature>();
	}
	
	public Feature[] getFeats() {
		return this.features.toArray(new Feature[this.features.size()]);
	}
	
	public boolean hasFeature(String id) {
		for(Feature f : this.features) {
			if(f.getID().equalsIgnoreCase(id)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String[] getFeatureNames() {
		List<String> ids = new ArrayList<String>();
		for(Feature f : this.features) {
			ids.add(f.getID());
		}
		return (String[]) ids.toArray();
	}
	
	public Feature getFeature(String id) {
		for(Feature f : this.features) {
			if(f.getID().equalsIgnoreCase(id)) {
				return f;
			}
		}
		
		return null;
	}
	
	public boolean addFeature(Feature feature) {
		if(!this.hasFeature(feature.getID()))
			return this.features.add(feature);
		
		return false;
	}
	
	public boolean removeFeature(Feature feature) {
		return this.features.remove(feature);
	}

	@Override
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		Feature f = getFeature(identifier);
		if(f != null) {
			return new Entry(Entry.Type.ENV, f);
		}
		
		throw new ExistenceException(pos);
	}

	@Override
	public boolean envPut(String identifier, Entry obj, int pos) throws InterpreterRuntimeException {
		throw new ImmutableException(pos);
	}

}
