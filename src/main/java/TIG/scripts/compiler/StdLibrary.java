package TIG.scripts.compiler;

import java.util.HashMap;

import TIG.scripts.Entry;
import TIG.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;
import TIG.utils.exceptions.interpreterExceptions.TypeException;

public class StdLibrary {

	protected static void interpretDisplay(Entry arg, HashMap<String, Entry> mem, int pos) throws InterpreterRuntimeException {
		String output = "";
		switch(arg.type) {
			case INTEGER:
			case DOUBLE:
			case BOOLEAN:
				output = "" + arg.val;
				break;
			case STRING:
				output = (String) arg.val;
				break;
			default:
				throw new TypeException(pos, "Unknown type given.");
		}
		
		System.out.println(output);
	}

}
