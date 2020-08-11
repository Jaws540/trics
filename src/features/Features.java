package features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.JSONUtils;
import utils.JSONify;

public class Features implements JSONify {
	
	private final List<Feature> feats;
	
	public Features() {
		this.feats = new ArrayList<Feature>();
	}
	
	/**
	 * List of Features
	 * @param feats - Feats to be added to this list
	 */
	public Features(Feature[] feats) {
		if(feats != null)
			this.feats = Arrays.asList(feats);
		else
			this.feats = new ArrayList<Feature>();
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

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("[\n");
		for(Feature f : this.feats) {
			output.append(indentString);
			output.append(f.toJSON(indent + 1));
			output.append(",\n");
		}
		if(this.feats.size() > 0) {
			// Remove trailing comma
			output.deleteCharAt(output.length() - 2);
			output.append(indentString.substring(0, indentString.length() - 1));
		}else {
			output.deleteCharAt(output.length() - 1);
		}
		output.append("]");
		
		return output.toString();
	}

}
