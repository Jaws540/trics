package TIG.utils;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	
	/**
	 * Validates an ID for use as a valid identifier in the program and for recognizability in scripts
	 * @param id - ID to test for validity
	 * @return A valid identifier.  If the id given is already a valid identifier, it will simply be returned.  If 
	 * an invalid identifier is given, this will return a valid identifier based on the given id.
	 */
	public static String validateID(String id) {
		String regex = "^" + Def.ID_REGEX + "$";
		if(Pattern.matches(regex, id)) {
			return id;
		}else {
			LOG.debug("Attempting to convert invalid ID '" + id + "' into a valid form");
			String out = id.replaceAll(Def.ID_REGEX_INV, "");
			if(!Pattern.matches(regex, out)) {
				LOG.debug("Last attempt to convert ID: '" + id + "' into a valid form");
				out = "__" + out;
			}
			LOG.debug("ID: '" + id + "' was converted to '" + out + "'");
			return out;
		}
	}

}
