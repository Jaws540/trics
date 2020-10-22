package characterModel.scripts;

import characterModel.utils.Def;

public class Entry {
	
	public enum Type {
		INTEGER(Def.INT),
		DOUBLE(Def.DOUBLE),
		BOOLEAN(Def.BOOL),
		STRING(Def.STRING),
		ENV("env"),
		SCRIPT("script");
		
		public final String name;
		
		private Type(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the corresponding basic type from the given string name
		 * @param name - Type name
		 * @return A Type enum or null if not given a basic type string name
		 */
		public static Type typeFromString(String name) {
			switch(name) {
				case Def.INT:
					return Type.INTEGER;
				case Def.DOUBLE:
					return Type.DOUBLE;
				case Def.BOOL:
					return Type.BOOLEAN;
				case Def.STRING:
					return Type.STRING;
				default:
					return null;
			}
		}
	}
	
	public final Type type;
	public Object val;
	public final boolean mutable;
	
	public Entry(Type type, Object val, boolean mutable) {
		this.type = type;
		this.val = val;
		this.mutable = mutable;
	}

}
