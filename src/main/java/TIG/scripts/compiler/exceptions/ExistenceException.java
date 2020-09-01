package TIG.scripts.compiler.exceptions;

public class ExistenceException extends InterpreterRuntimeException {

	public ExistenceException() {
		super("Unknown identifier.");
	}

}
