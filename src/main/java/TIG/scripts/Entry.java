package TIG.scripts;

public class Entry {
	
	public enum Type {
		INT,
		DOUBLE,
		BOOL,
		STRING,
		ENV,
		FEAT,
		FIELD,
	}
	
	public final Type type;
	public final Object obj;
	
	public Entry(Type type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

}
