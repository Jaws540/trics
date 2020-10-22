package characterModel.utils.exceptions.interpreterExceptions;

public class ImmutableException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public ImmutableException(int pos) {
		super("Identifier points to an immutable value.", pos);
	}

}
