package TIG.scripts.compiler.exceptions;

public class SyntaxException extends CompileException {
	
	private static final long serialVersionUID = 1L;

	public SyntaxException(String message, int pos) {
		super("Unexpected Syntax.  " + message, pos);
	}

}
