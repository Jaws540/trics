package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.features.Features;

public class FeaturesSerializer implements JsonSerializer<Features>, JsonDeserializer<Features> {

	@Override
	public JsonElement serialize(Features src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getFeats());
	}

	@Override
	public Features deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
