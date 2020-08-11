package tags;

import java.util.Arrays;
import java.util.List;

public abstract class Taggable {
	
	private List<Tag> tags;
	
	public Taggable(Tag[] tags) {
		this.tags = Arrays.asList(tags);
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

}
