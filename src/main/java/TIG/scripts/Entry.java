package TIG.scripts;

public class Entry {
	
	public enum Type {
		INT(Def.INT),
		DOUBLE(Def.DOUBLE),
		BOOL(Def.BOOL),
		STRING(Def.STRING),
		ENV("env");
		
		public final String name;
		
		private Type(String name) {
			this.name = name;
		}
	}
	
	public final Type type;
	public Object val;
	
	public Entry(Type type, Object val) {
		this.type = type;
		this.val = val;
	}

}
