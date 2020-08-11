package characterSheet.notes;

import java.util.Arrays;
import java.util.List;

public class Notes {
	
	private final List<NoteTree> notes;
	
	public Notes(NoteTree[] notes) {
		this.notes = Arrays.asList(notes);
	}

}
