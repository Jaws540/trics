package characterSheet.notes;

public abstract class NoteTree {

	private String title;
	
	public NoteTree(String title) {
		this.setTitle(title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
