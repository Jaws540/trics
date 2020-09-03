package TIG.scripts.compiler.exceptions;

public class PositionalException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public int position;
	
	public PositionalException(String msg, int pos) {
		super(msg);
		this.position = pos;
	}

}
