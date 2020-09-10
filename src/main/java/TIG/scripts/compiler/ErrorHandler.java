package TIG.scripts.compiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TIG.utils.exceptions.PositionalException;
import TIG.utils.exceptions.compileExceptions.CompileException;
import TIG.utils.exceptions.compileExceptions.ExceptionList;
import TIG.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;

public class ErrorHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(ErrorHandler.class);
	
	private static String fixedLengthString(String string, int length) {
	    return String.format("%1$"+length+ "s", string);
	}
	
	private static void handleException(PositionalException e, String src) {
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
		System.err.println("-------------------------------------");
		if(e instanceof CompileException)
			System.err.print("Syntax ");
		else if(e instanceof InterpreterRuntimeException)
			System.err.print("Runtime ");
		System.err.println("Error on line " + (i + 1));
		System.err.println(prefix + line);
		System.err.println(fixedLengthString(" ", numSpaces) + "^");
		System.err.println(e.getMessage());
		
		// Log error messages
		LOG.error("-------------------------------------");
		if(e instanceof CompileException)
			LOG.error("Syntax ");
		else if(e instanceof InterpreterRuntimeException)
			LOG.error("Runtime ");
		LOG.error("Error on line " + (i + 1));
		LOG.error(prefix + line);
		LOG.error(fixedLengthString(" ", numSpaces) + "^");
		LOG.error(e.getMessage());
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

	public static void handleInterpreterException(InterpreterRuntimeException e, String src) {
		handleException(e, src);
	}

	public static void handleUnknown(int pos, String src) {
		PositionalException e = new PositionalException("Unknown error.", pos);
		handleException(e, src);
	}

}
