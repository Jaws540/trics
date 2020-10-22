package characterModel.bundler;

import java.util.ArrayList;
import java.util.List;

/**
 *  Singleton class that handles all installed bundles
 * @author QuantumRetrofit
 *
 */
public class Bundler {
	
	private static Bundler instance = null;
	static {
		instance = new Bundler();
	}
	
	private List<Bundle> bundles;
	
	// Hide constructor
	private Bundler() {
		bundles = new ArrayList<Bundle>();
	}
	
	/**
	 * Gets the singleton instance for the Bundler
	 * @return
	 */
	public static Bundler getInstance() {
		if(instance == null)
			instance = new Bundler();
		
		return instance;
	}
	
	// Adds the bundle to the registered set of bundles
	public boolean registerBundle(Bundle bundle) {
		if(bundles.contains(bundle))
			return false;
		
		bundles.add(bundle);
		return true;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public Bundle getBundle(String name) {
		for(Bundle b : bundles) {
			if(b.equals(name))
				return b;
		}
		
		return null;
	}

}
