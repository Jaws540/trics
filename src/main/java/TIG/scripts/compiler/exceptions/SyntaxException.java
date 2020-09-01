package TIG.scripts.compiler.exceptions;

public class SyntaxException extends CompileException {
	
	public SyntaxException(String message, int pos) {
		super("Unexpected Syntax.  " + message, pos);
	}

}
