package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.tags.Taggable;

public class TaggableSerializer implements JsonSerializer<Taggable>, JsonDeserializer<Taggable> {

	@Override
	public JsonElement serialize(Taggable src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getTags());
	}

	@Override
	public Taggable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
