package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.scripts.Script;
import TIG.scripts.Scripts;

public class ScriptsSerializer implements JsonSerializer<Scripts>, JsonDeserializer<Scripts> {

	@Override
	public JsonElement serialize(Scripts src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getScripts());
	}

	@Override
	public Scripts deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray scripts = json.getAsJsonArray();
		
		List<Script> scriptList = new ArrayList<>();
		for(JsonElement j : scripts) {
			Script script = context.deserialize(j, Script.class);
			scriptList.add(script);
		}
		
		return new Scripts(scriptList.toArray(new Script[scriptList.size()]));
	}

}
