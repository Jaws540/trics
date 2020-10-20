package TIG.utils.exceptions.compileExceptions;

public class InvalidEscapeSequenceException extends CompileException {
	
	private static final long serialVersionUID = 1L;

	public InvalidEscapeSequenceException(int pos) {
		super("Invalid escape sequence.", pos);
	}

}
