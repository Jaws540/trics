package characterModel.utils.exceptions.interpreterExceptions;

public class ExistenceException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public ExistenceException(int pos) {
		super("Unknown identifier.", pos);
	}

}
