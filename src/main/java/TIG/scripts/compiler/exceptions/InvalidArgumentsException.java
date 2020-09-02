package TIG.scripts.compiler.exceptions;

public class InvalidArgumentsException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidArgumentsException() {
		super("Invalid arguments.");
	}

	public InvalidArgumentsException(String msg) {
		super("Invalid arguments.  " + msg);
	}

}
