package characterSheet.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Features {
	
	private final List<Feature> feats;
	
	/**
	 * List of Features
	 * @param feats - Feats to be added to this list
	 */
	public Features(Feature[] feats) {
		this.feats = Arrays.asList(feats);
	}
	
	public Feature[] getFeats() {
		return (Feature[]) this.feats.toArray();
	}
	
	public boolean hasFeature(String featureName) {
		for(Feature f : this.feats) {
			if(f.getName().equalsIgnoreCase(featureName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String[] getFeatureNames() {
		List<String> names = new ArrayList<String>();
		for(Feature f : this.feats) {
			names.add(f.getName());
		}
		return (String[]) names.toArray();
	}
	
	public Feature getFeature(String featureName) {
		for(Feature f : this.feats) {
			if(f.getName().equalsIgnoreCase(featureName)) {
				return f;
			}
		}
		
		return null;
	}
	public boolean addFeature(Feature feature) {
		if(!this.hasFeature(feature.getName())) {
			return this.feats.add(feature);
		}
		
		return false;
	}
	
	public boolean removeFeature(Feature feature) {
		return this.feats.remove(feature);
	}

}
