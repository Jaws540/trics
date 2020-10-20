package TIG.utils.exceptions.interpreterExceptions;

public class TypeException extends InterpreterRuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TypeException(int pos, String msg) {
		super("Mismatched Type.  " + msg , pos);
	}

}
