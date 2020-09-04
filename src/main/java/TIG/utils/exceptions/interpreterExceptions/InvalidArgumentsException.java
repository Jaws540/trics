package TIG.utils.exceptions.interpreterExceptions;

public class InvalidArgumentsException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidArgumentsException(int pos) {
		super("Invalid arguments.", pos);
	}

	public InvalidArgumentsException(String msg, int pos) {
		super("Invalid arguments.  " + msg, pos);
	}

}
