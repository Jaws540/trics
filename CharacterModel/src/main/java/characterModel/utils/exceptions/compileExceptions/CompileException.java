package characterModel.utils.exceptions.compileExceptions;

import characterModel.utils.exceptions.PositionalException;

public abstract class CompileException extends PositionalException {
	
	private static final long serialVersionUID = 1L;
	
	public CompileException(String msg, int pos) {
		super(msg, pos);
	}

}
