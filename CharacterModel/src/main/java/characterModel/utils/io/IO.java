package characterModel.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import characterModel.utils.Def;

public final class IO {

	private static final Logger LOG = LoggerFactory.getLogger(IO.class);
	
	/**
	 * Saves a byte array to the specified path
	 * @param bytes - Data to save
	 * @param path - Absolute path to save file
	 * @return true on success, false otherwise
	 */
	protected static boolean saveBytes(byte[] bytes, String path) {
		File saveFile = new File(path);
		LOG.debug("Saving data to '" + path + "'");
		try {
			if(!saveFile.exists()) {
				LOG.debug("Creating new save file");
				saveFile.createNewFile();
			}
			
			OutputStream os = new FileOutputStream(saveFile);
			os.write(bytes);
			os.flush();
			os.close();
			
			return true;
		} catch (IOException e) {
			LOG.error("Failed to save data to file '" + path + "'");
			return false;
		}
	}
	
	/**
	 * Loads a byte array from the specified path
	 * @param path - Absolute path to the saved file
	 * @return A byte array on success, null otherwise
	 */
	protected static byte[] loadBytes(String path) {
		File saveFile = new File(path);
		LOG.debug("Loading data from '" + path + "'");
		try {
			return Files.readAllBytes(saveFile.toPath());
		} catch (IOException e) {
			LOG.error("Failed to read data from file");
			return null;
		}
	}
	
	/**
	 * Convert a string into a useable file name.  A file name must only contain the characters 
	 * [A-Za-z0-9_!@#$%^&()'{}[],`~-+=].
	 * @param rawName - A string potentially containing invalid characters and NOT containing any file extension or path
	 * @return A string containing only characters from the above character set
	 */
	protected static String toFileName(String rawName) {
		String regex = "^" + Def.FILENAME_REGEX + "$";
		if(Pattern.matches(regex, rawName)) {
			return rawName;
		}else {
			LOG.debug("Attempting to convert invalid filename '" + rawName + "' into a valid form");
			String out = rawName.replaceAll(Def.FILENAME_REGEX_INV, "_");
			LOG.debug("Filename: '" + rawName + "' was converted to '" + out + "'");
			return out;
		}
	}
	
	/**
	 * Gets the directory path of a file/directory path
	 * @param path - Any file/directory path
	 * @return A string of just the directory portion of the given path or null if no parent directory is present
	 */
	public static String getDirectoryPath(String path) {		
		if(path == null)
			return null;
		
		File tmp = new File(path);
		
		String out = null;
	
		// Get the parent directory if a file path is given
		String name = tmp.getName();
		if(name.matches(Def.FILE_REGEX)) {
			out = tmp.getParent();
			
		// Otherwise assume a directory path was given
		}else {
			out = path;
		}
		
		// Ensure the resultant path ends with a system separator
		if(out != null && !out.endsWith(File.separator)) {
			out += File.separator;
		}
		
		return out;
	}
	
	/**
	 * Gets the file name of a file/directory path
	 * @param path - Any file/directory path
	 * @return A string of the file name without an extension or null if the path points to a directory or this pathname's 
	 * name sequence is empty
	 */
	public static String getFileName(String path) {
		if(path == null)
			return null;
		
		File tmp = new File(path);
		String name = tmp.getName();
		if(name.matches(Def.FILE_REGEX)) {
			return name.substring(0, name.lastIndexOf('.'));
		}
		
		return null;
	}
	
	/**
	 * Pulls a file extension out of a given path
	 * @param path
	 * @return
	 */
	protected static String getExtenstion(String path) {
		if(path == null)
			return null;
		
		Matcher m = Pattern.compile("^.+(\\.[a-zA-Z0-9]+)$").matcher(path);
		if(m.matches()) {
			return m.group(1);
		}
		
		return null;
	}
	
	public static String loadView(String path) {
		if(path == null || path.isEmpty() || path.isBlank())
			return null;
		
		byte[] data = loadBytes(path);
		return new String(data);
	}
}
