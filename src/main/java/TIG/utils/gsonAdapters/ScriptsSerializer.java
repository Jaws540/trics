package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.scripts.Scripts;

public class ScriptsSerializer implements JsonSerializer<Scripts>, JsonDeserializer<Scripts> {

	@Override
	public JsonElement serialize(Scripts src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getScripts());
	}

	@Override
	public Scripts deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
