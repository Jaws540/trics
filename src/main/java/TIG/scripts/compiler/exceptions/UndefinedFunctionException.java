package TIG.scripts.compiler.exceptions;

public class UndefinedFunctionException extends InterpreterRuntimeException {

	private static final long serialVersionUID = 1L;

	public UndefinedFunctionException() {
		super("Undefined function.");
	}
	
	public UndefinedFunctionException(String fnName) {
		super("Undefined function '" + fnName + "'.");
	}

}
