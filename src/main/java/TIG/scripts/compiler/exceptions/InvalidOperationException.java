package TIG.scripts.compiler.exceptions;

public class InvalidOperationException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidOperationException() {
		super("Invalid operation.");
	}

}
