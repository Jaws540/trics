package characterModel.utils.exceptions.interpreterExceptions;

public class UndefinedFunctionException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public UndefinedFunctionException(int pos) {
		super("Undefined function.", pos);
	}
	
	public UndefinedFunctionException(String fnName, int pos) {
		super("Undefined function '" + fnName + "'.", pos);
	}

}
