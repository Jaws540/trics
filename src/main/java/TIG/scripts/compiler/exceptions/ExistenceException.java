package TIG.scripts.compiler.exceptions;

public class ExistenceException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistenceException() {
		super("Unknown identifier.");
	}

}
