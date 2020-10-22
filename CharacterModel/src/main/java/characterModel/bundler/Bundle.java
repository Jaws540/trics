package characterModel.bundler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import characterModel.features.Feature;
import characterModel.scripts.Script;

public class Bundle {
	
	private static final Logger LOG = LoggerFactory.getLogger(Bundle.class);
	
	private final String bundleName;
	
	private final List<Feature> features;
	private final List<Script> scripts;
	private final List<String> pages;
	
	public Bundle(String name, List<Feature> features, List<Script> scripts, List<String> pages) {
		this.bundleName = name;
		this.features = features;
		this.scripts = scripts;
		this.pages = pages;
	}
	
	public String getName() {
		return bundleName;
	}
	
	@Override
	public boolean equals(Object obj) {
		String otherName = null;
		if(obj instanceof Bundle) {
			otherName = ((Bundle) obj).bundleName;
		}else if(obj instanceof String) {
			otherName = (String) obj;
		}
		
		return bundleName.equalsIgnoreCase(otherName);
	}
	
	@Override
	public int hashCode() {
		return bundleName.hashCode();
	}
	
	@Override
	public String toString() {
		return bundleName;
	}

}
