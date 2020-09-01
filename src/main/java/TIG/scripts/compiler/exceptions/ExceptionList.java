package TIG.scripts.compiler.exceptions;

import java.util.List;

public class ExceptionList extends Throwable {
	
	public List<? extends CompileException> list;
	
	public ExceptionList(List<? extends CompileException> lst) {
		this.list = lst;
	}

}
