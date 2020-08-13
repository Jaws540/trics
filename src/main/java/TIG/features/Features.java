package TIG.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Features {
	
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
	
	public boolean hasFeature(String featureName) {
		for(Feature f : this.features) {
			if(f.getName().equalsIgnoreCase(featureName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String[] getFeatureNames() {
		List<String> names = new ArrayList<String>();
		for(Feature f : this.features) {
			names.add(f.getName());
		}
		return (String[]) names.toArray();
	}
	
	public Feature getFeature(String featureName) {
		for(Feature f : this.features) {
			if(f.getName().equalsIgnoreCase(featureName)) {
				return f;
			}
		}
		
		return null;
	}
	public boolean addFeature(Feature feature) {
		if(!this.hasFeature(feature.getName())) {
			return this.features.add(feature);
		}
		
		return false;
	}
	
	public boolean removeFeature(Feature feature) {
		return this.features.remove(feature);
	}

}
