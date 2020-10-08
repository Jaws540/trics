package TIG.utils.bundler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bundle {
	
	private static final Logger LOG = LoggerFactory.getLogger(Bundle.class);
	
	private static final String baseBundlePath = "bundles/";
	private static final String bundleExtension = ".rpgb";
	private static final File bundleDir;
	
	static {
		bundleDir = new File(baseBundlePath);
		if(!bundleDir.exists()) {
			LOG.info("Creating base bundle directory");
			bundleDir.mkdirs();
		}
	}
	
	private final String bundleName;
	
	public Bundle(String name) {
		this.bundleName = name;
	}
	
	public static void test() {
		LOG.info(bundleDir.getAbsolutePath());
	}

}
