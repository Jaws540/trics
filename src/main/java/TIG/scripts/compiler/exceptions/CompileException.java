package TIG.scripts.compiler.exceptions;

public abstract class CompileException extends Exception {
	
	public int position;
	
	public CompileException(String msg, int pos) {
		super(msg);
		this.position = pos;
	}

}
