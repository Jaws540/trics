package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import TIG.notes.Note;
import TIG.notes.NoteElement;
import TIG.notes.NoteTree;

public class NoteElementSerializer implements JsonDeserializer<NoteElement> {

	@Override
	public NoteElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		if(obj.has("children")) {
			// NoteElement is a NoteTree
			return context.deserialize(json, NoteTree.class);
		}else {
			// NoteElement is a Note
			return context.deserialize(json, Note.class);
		}
	}

}
