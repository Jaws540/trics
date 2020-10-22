package characterModel.utils.exceptions.compileExceptions;

public class UnknownSymbolException extends CompileException {
	
	private static final long serialVersionUID = 1L;

	public UnknownSymbolException(int pos) {
		super("Unknown symbol.", pos);
	}

}
