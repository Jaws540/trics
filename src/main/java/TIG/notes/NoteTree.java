package TIG.notes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteTree extends NoteElement {
	
	private final List<NoteElement> children;
	
	public NoteTree(String title) throws Exception {
		super(title);
		this.children = new ArrayList<NoteElement>();
	}
	
	/**
	 * This provides the folder structure to a note tree.
	 * 
	 * Note: This class should NOT be used as the root node for a Note tree.  Instead use the Notes class.
	 * @param title - Folder title
	 * @param children - Branches of this node
	 * @throws Exception 
	 */
	public NoteTree(String title, NoteTree[] children) throws Exception {
		super(title);
		
		if(children != null)
			this.children = Arrays.asList(children);
		else
			this.children = new ArrayList<NoteElement>();
	}
	
	public boolean addNote(Note note) {
		return this.children.add(note);
	}
	
	public boolean remvoeNote(Note note) {
		return this.children.remove(note);
	}
	
	public boolean addFolder(NoteTree folder) {
		
		return this.children.add(folder);
	}
	
	public boolean removeFolder(NoteTree folder) {
		return this.children.remove(folder);
	}
	
	public NoteTree[] getChildren() {
		return (NoteTree[]) this.children.toArray();
	}

}
