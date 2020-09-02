package TIG.scripts.compiler.exceptions;

public class ImmutableException extends InterpreterRuntimeException {

	public ImmutableException() {
		super("Identifier points to an immutable value.");
	}

}
