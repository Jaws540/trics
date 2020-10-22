package characterModel.utils.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptIO {

	private static final Logger LOG = LoggerFactory.getLogger(ScriptIO.class);
	
	/**
	 * Loads a script from the path
	 * @param src_path - Absolute path to the script file (usually points to a script in a bundle)
	 * @return The raw source string of the script on success, null otherwise
	 */
	public static String loadScript(String src_path) {
		LOG.debug("Loading script from '" + src_path + "'");
		byte[] raw = IO.loadBytes(src_path);
		if(raw == null) {
			LOG.error("Failed to read script data from file '" + src_path + "'");
			return null;
		}
		
		return new String(raw);
	}

}
