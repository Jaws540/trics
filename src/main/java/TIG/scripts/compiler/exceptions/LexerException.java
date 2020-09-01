package TIG.scripts.compiler.exceptions;

public class LexerException extends CompileException {
	
	public LexerException(int pos) {
		super("Unknown text.", pos);
	}

}
