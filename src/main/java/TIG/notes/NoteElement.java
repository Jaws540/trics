package TIG.notes;

public abstract class NoteElement {
	
	private String title;
	
	public NoteElement(String title) throws Exception {
		if(title != null && !title.isEmpty()) {
			this.title = title;
		}else {
			throw new Exception("Invalid NoteElement title!");
		}
			
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}
