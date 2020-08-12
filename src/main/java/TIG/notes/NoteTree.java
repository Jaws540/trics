package TIG.notes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import TIG.utils.JSONUtils;
import TIG.utils.JSONify;

public class NoteTree extends NoteElement implements JSONify {
	
	private final List<NoteElement> children;
	
	public NoteTree(String title) {
		super(title);
		this.children = new ArrayList<NoteElement>();
	}
	
	/**
	 * This provides the folder structure to a note tree.
	 * 
	 * Note: This class should NOT be used as the root node for a Note tree.  Instead use the Notes class.
	 * @param title - Folder title
	 * @param children - Branches of this node
	 */
	public NoteTree(String title, NoteTree[] children) {
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

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Title", this.getTitle()));
		output.append(",\n");
		output.append(indentString);
		output.append("\"Children\":  [\n");
		String indentString2 = JSONUtils.getIndent(indent + 1);
		for(NoteElement n : this.children) {
			output.append(indentString2);
			output.append(n.toJSON(indent + 2));
			output.append(",\n");
		}
		if(this.children.size() > 0) {
			// Remove trailing comma
			output.deleteCharAt(output.length() - 2);
			output.append(indentString);
		}else {
			output.deleteCharAt(output.length() - 1);
		}
		output.append("]\n");
		output.append(indentString.substring(0, indentString.length() - 1));
		output.append("}");
		
		return output.toString();
	}

}
