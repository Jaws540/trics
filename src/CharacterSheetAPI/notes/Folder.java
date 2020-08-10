package CharacterSheetAPI.notes;

import java.util.Arrays;
import java.util.List;

public class Folder {
	
	private String title;
	private final List<Note> notes;
	
	public Folder(String title, Note[] notes) {
		this.setTitle(title);
		this.notes = Arrays.asList(notes);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Note> getNotes() {
		return notes;
	}

}
