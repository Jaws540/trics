package TIG.scripts.compiler;

import TIG.scripts.compiler.exceptions.CompileException;
import TIG.scripts.compiler.exceptions.ExceptionList;

public class ErrorHandler {
	
	private static String fixedLengthString(String string, int length) {
	    return String.format("%1$"+length+ "s", string);
	}
	
	private static void handleException(CompileException e, String src) {
		String[] lines = src.split("\n");
		String test = src.substring(0, e.position);
		
		// Find the line that contains the error
		// (count the '\n' characters)
		int i = 0;
		int pos = 0;
		while(pos < test.length()) {
			if(test.charAt(pos) == '\n')
				i++;
			
			pos++;
		}
		
		// Calculate initial spacing for carret
		String tmp = test.substring(test.lastIndexOf('\n'));
		int errorStart = Token.WHITESPACE.matches(tmp);
		int errorOffset = tmp.substring(errorStart).length();
		
		// To print
		String prefix = "Line " + (i + 1) + ": ";
		String line = lines[i].substring(errorStart - 1).trim();
		
		// Final spacing for carret
		int numSpaces = prefix.length() + errorOffset;
		
		// Print error messages
		System.err.println("Error on line " + (i + 1));
		System.err.println(prefix + line);
		System.err.println(fixedLengthString(" ", numSpaces) + "^");
		System.err.println(e.getMessage());
	}
	
	public static void handleCompileException(CompileException e, String src) {
		handleException(e, src);
	}

	public static void handleExceptionList(ExceptionList elst, String src) {
		for(int i = 0; i < elst.list.size(); i++) {
			CompileException e = elst.list.get(i);
			handleException(e, src);
			if(i != elst.list.size() - 1) {
				System.err.println();
			}
		}
	}

}
