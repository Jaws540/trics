package TIG.utils.exceptions.compileExceptions;

public class SyntaxException extends CompileException {
	
	private static final long serialVersionUID = 1L;

	public SyntaxException(String message, int pos) {
		super("Unexpected syntax.  " + message, pos);
	}

}
