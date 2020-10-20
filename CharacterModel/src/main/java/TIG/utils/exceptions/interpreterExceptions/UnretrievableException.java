package TIG.utils.exceptions.interpreterExceptions;

public class UnretrievableException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public UnretrievableException(int pos) {
		super("Identifier cannot be resolved to a type.", pos);
	}

}
