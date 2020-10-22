package characterModel.utils.exceptions.interpreterExceptions;

import characterModel.utils.exceptions.PositionalException;

public class InterpreterRuntimeException extends PositionalException {
	
	private static final long serialVersionUID = 1L;

	public InterpreterRuntimeException(String msg, int pos) {
		super(msg, pos);
	}

}
