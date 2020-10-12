package TIG.bundler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bundle {
	
	private static final Logger LOG = LoggerFactory.getLogger(Bundle.class);
	
	private final String bundleName;
	
	public Bundle(String name) {
		this.bundleName = name;
	}
	
	public String getName() {
		return bundleName;
	}

}
