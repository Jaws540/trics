package tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.JSONUtils;
import utils.JSONify;

public abstract class Taggable implements JSONify{
	
	private List<Tag> tags;
	
	public Taggable() {
		this.tags = new ArrayList<Tag>();
	}
	
	public Taggable(Tag[] tags) {
		if(tags != null)
			this.tags = Arrays.asList(tags);
		else
			this.tags = new ArrayList<Tag>();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean isTagged(String text) {
		for(Tag t : this.tags) {
			if(t.equals(text)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean tag(String text) {
		Tag newTag = new Tag(text);
		
		// Ensure the tag is not already added
		if(!this.isTagged(text)) {
			// Add tag and return true
			this.tags.add(newTag);
			return true;
		}
		
		// Failed to add tag
		return false;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public Tag untag(String text) {
		// Get the tag object from the list
		Tag oldTag = null;
		for(Tag t : this.tags) {
			if(t.equals(text)) {
				oldTag = t;
				break;
			}
		}
		
		// If the tag exists, remove it and return the value
		if(oldTag != null) {
			this.tags.remove(oldTag);
			return oldTag;
		}
		
		// Otherwise, return null
		return null;
	}
	
	public Tag[] getTags() {
		return (Tag[]) this.tags.toArray();
	}

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("[\n");
		for(Tag t : this.tags) {
			output.append(indentString);
			output.append(t.toJSON(indent + 1));
			output.append(",\n");
		}
		if(this.tags.size() > 0) {
			// Remove trailing comma
			output.deleteCharAt(output.length() - 2);
			output.append(indentString.substring(0, indentString.length() - 1));
		}else {
			output.deleteCharAt(output.length() - 1);
		}
		output.append("]");
		
		return output.toString();
	}
	
	

}
