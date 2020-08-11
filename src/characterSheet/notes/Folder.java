package characterSheet.notes;

import java.util.Arrays;
import java.util.List;

public class Folder extends NoteTree {
	
	private final List<NoteTree> children;
	
	public Folder(String title, NoteTree[] children) {
		super(title);
		this.children = Arrays.asList(children);
	}

	public NoteTree[] getChildren() {
		return (NoteTree[]) this.children.toArray();
	}

}
