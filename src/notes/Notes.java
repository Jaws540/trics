package notes;

public class Notes extends NoteTree {
	
	public Notes() {
		super("root");
	}
	
	/**
	 * Base entry into a Note Hierarchy.
	 * 
	 * This class should be used as the root node for the Notes tree instead of a NoteTree node.
	 * @param notes
	 */
	public Notes(NoteTree[] notes) {
		super("root", notes);
	}

}
