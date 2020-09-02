package TIG.scripts.compiler.exceptions;

public abstract class CompileException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public int position;
	
	public CompileException(String msg, int pos) {
		super(msg);
		this.position = pos;
	}

}
