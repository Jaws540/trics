package TIG.scripts.compiler.exceptions;

public class ImmutableException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public ImmutableException() {
		super("Identifier points to an immutable value.");
	}

}
