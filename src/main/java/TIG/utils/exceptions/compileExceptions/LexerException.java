package TIG.scripts.compiler.exceptions.compileExceptions;

public class LexerException extends CompileException {
	
	private static final long serialVersionUID = 1L;

	public LexerException(int pos) {
		super("Unknown symbol.", pos);
	}

}
