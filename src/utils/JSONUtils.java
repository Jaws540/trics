package utils;

public class JSONUtils {
	
	public static String getIndent(int indent) {
		return new String(new char[indent]).replace("\0", "\t");
	}
	
	public static String basicJSONifyJSON(String name, String value) {
		return "\"" + name + "\": " + value.toString();
	}
	
	public static String basicJSONify(String name, String value) {
		return "\"" + name + "\": \"" + value.toString() + "\"";
	}
	
	public static String basicJSONify(String name, Integer value) {
		return "\"" + name + "\": \"" + value.toString() + "\"";
	}
	
	public static String basicJSONify(String name, Double value) {
		return "\"" + name + "\": \"" + value.toString() + "\"";
	}
	
	public static String basicJSONify(String name, Boolean value) {
		return "\"" + name + "\": \"" + value.toString() + "\"";
	}

}
