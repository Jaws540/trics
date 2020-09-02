package TIG.scripts.compiler.exceptions;

public class LexerException extends CompileException {
	
	private static final long serialVersionUID = 1L;

	public LexerException(int pos) {
		super("Unknown text.", pos);
	}

}
