package TIG.notes;

import java.util.Date;

public class Note extends NoteElement {
	
	private String text;
	private final Date date;
	
	public Note(String title, String text, Date date) throws Exception {
		super(title);
		this.setText(text);
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

}
