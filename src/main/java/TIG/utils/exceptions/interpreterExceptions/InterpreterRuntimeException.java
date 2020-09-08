package TIG.utils.exceptions.interpreterExceptions;

import TIG.utils.exceptions.PositionalException;

public class InterpreterRuntimeException extends PositionalException {
	
	private static final long serialVersionUID = 1L;

	public InterpreterRuntimeException(String msg, int pos) {
		super(msg, pos);
	}

}
