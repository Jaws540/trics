package characterSheet.notes;

public abstract class NoteElement {
	
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
