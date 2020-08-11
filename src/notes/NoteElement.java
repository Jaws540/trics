package notes;

import utils.JSONify;

public abstract class NoteElement implements JSONify {
	
	private String title;
	
	public NoteElement(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}
