package TIG.notes;

public class Note extends NoteElement {
	
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

}
