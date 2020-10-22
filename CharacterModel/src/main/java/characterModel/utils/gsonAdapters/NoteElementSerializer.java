package characterModel.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import characterModel.notes.Note;
import characterModel.notes.NoteElement;
import characterModel.notes.NoteTree;

public class NoteElementSerializer implements JsonSerializer<NoteElement>, JsonDeserializer<NoteElement> {

	@Override
	public JsonElement serialize(NoteElement src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src);
	}

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
