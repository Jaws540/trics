package notes;

import utils.JSONUtils;
import utils.JSONify;

public class Note extends NoteElement implements JSONify {
	
	private String text;
	
	public Note(String title, String text) {
		super(title);
		this.setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toJSON(int indent) {
		
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Title", this.getTitle()));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Text", this.text));
		output.append("\n");
		output.append(indentString.substring(0, indentString.length() - 1));
		output.append("}");
		
		return output.toString();
		/*
		* Decided not to use the below code instead for now...
		*/
		
		//return JSONUtils.basicJSONify(this.getTitle(), this.text);
	}

}
