package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.scripts.Script;

public class ScriptSerializer implements JsonSerializer<Script>, JsonDeserializer<Script> {

	@Override
	public JsonElement serialize(Script src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject base = new JsonObject();
		base.add("id", context.serialize(src.getID()));
		base.add("path", context.serialize(src.getSourcePath()));
		base.add("displayName", context.serialize(src.getDisplayName()));
		base.add("description", context.serialize(src.getDescription()));
		return base;
	}

	@Override
	public Script deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		String id = context.deserialize(obj.get("id"), String.class);
		String path = context.deserialize(obj.get("path"), String.class);
		String displayName = context.deserialize(obj.get("displayName"), String.class);
		String description = context.deserialize(obj.get("description"), String.class);
		return new Script(id, path, displayName, description);
	}

}
