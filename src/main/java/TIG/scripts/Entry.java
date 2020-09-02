package TIG.scripts;

public class Entry {
	
	public enum Type {
		INT,
		DOUBLE,
		BOOL,
		STRING,
		ENV,
	}
	
	public final Type type;
	public final Object val;
	
	public Entry(Type type, Object val) {
		this.type = type;
		this.val = val;
	}

}
