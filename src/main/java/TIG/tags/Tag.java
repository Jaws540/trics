package TIG.tags;

public class Tag {
	
	private final String tag;
	
	/**
	 * A special String wrapper that converts the text to all lower case and the first character to upper case.
	 * @param text - non-null, non-empty string text for the tag
	 * @throws NullPointerException
	 */
	public Tag(String text) throws Exception {
		if(text != null && !text.isEmpty()) {
			text = text.substring(0, 1).toUpperCase() + text.substring(1);
			this.tag = text;
		}else {
			throw new Exception("Invalid tag text.");
		}
	}
	
	public String getText() {
		return this.tag;
	}
	
	@Override
	public String toString() {
		return this.tag;
	}

}
