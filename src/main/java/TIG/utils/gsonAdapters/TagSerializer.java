package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.tags.Tag;

public class TagSerializer implements JsonSerializer<Tag>, JsonDeserializer<Tag> {

	@Override
	public JsonElement serialize(Tag src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getText());
	}

	@Override
	public Tag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return new Tag(context.deserialize(json, String.class));
	}

}
