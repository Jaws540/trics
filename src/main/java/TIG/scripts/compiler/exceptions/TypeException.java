package TIG.scripts.compiler.exceptions;

public class TypeException extends InterpreterRuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TypeException() {
		super("Mismatched Type.");
	}

}
