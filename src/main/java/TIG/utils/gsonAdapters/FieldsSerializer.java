package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.features.Field;
import TIG.features.Fields;

public class FieldsSerializer implements JsonSerializer<Fields>, JsonDeserializer<Fields> {

	@Override
	public JsonElement serialize(Fields src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject fields = new JsonObject();
		for(Field<?> f : src.getFields()) {
			fields.add(f.getIdentifier(), context.serialize(f.getValue()));
		}
		return fields;
	}

	@Override
	public Fields deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
