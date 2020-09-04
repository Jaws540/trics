package TIG.scripts.compiler.exceptions.interpreterExceptions;

import TIG.scripts.compiler.exceptions.PositionalException;

public class InterpreterRuntimeException extends PositionalException {
	
	private static final long serialVersionUID = 1L;

	public InterpreterRuntimeException(String msg, int pos) {
		super(msg, pos);
	}

}
