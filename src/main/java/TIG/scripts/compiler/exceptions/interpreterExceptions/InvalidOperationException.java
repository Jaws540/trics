package TIG.scripts.compiler.exceptions.interpreterExceptions;

public class InvalidOperationException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidOperationException(int pos) {
		super("Invalid operation.", pos);
	}

}
