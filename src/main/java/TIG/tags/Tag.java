package TIG.tags;

import TIG.utils.JSONify;

public class Tag implements JSONify {
	
	private final String tag;
	
	/**
	 * A special String wrapper that converts the text to all lower case and the first character to upper case.
	 * @param text - non-null, non-empty string text for the tag
	 * @throws NullPointerException
	 */
	public Tag(String text) throws NullPointerException {
		if(text != null && !text.isEmpty()) {
			text = text.substring(0, 1).toUpperCase() + text.substring(1);
			this.tag = text;
		}else {
			throw new NullPointerException("A Tag must be a non-empty string!");
		}
	}
	
	public String getText() {
		return this.tag;
	}
	
	@Override
	public String toString() {
		return this.tag;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Tag) {
			// If the object is a Tag, compare the tag fields directly.
			Tag obj = (Tag) o;
			return this.tag.equals(obj.tag);
		}else if(o instanceof String) {
			// If the object is a String, compare this tag field to the tag field the String would create.
			String obj = (String) o;
			return this.tag.equals(obj.substring(0, 1).toUpperCase() + obj.substring(1));
		}
		
		// If the object is not a Tag or String, equality is not defined and should default to false
		return false;
	}

	@Override
	public String toJSON(int indent) {
		return "\"" + this.tag + "\"";
	}

}
