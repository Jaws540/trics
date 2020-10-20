package TIG.notes;

public class Notes extends NoteTree {
	
	public Notes() throws Exception {
		super("root");
	}
	
	/**
	 * Base entry into a Note Hierarchy.
	 * 
	 * This class should be used as the root node for the Notes tree instead of a NoteTree node.
	 * @param notes
	 * @throws Exception 
	 */
	public Notes(NoteTree[] notes) throws Exception {
		super("root", notes);
	}

}
