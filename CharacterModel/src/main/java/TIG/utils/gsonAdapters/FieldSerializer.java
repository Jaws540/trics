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
import TIG.scripts.Entry;

public class FieldSerializer implements JsonSerializer<Field>, JsonDeserializer<Field> {

	@Override
	public JsonElement serialize(Field src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject base = new JsonObject();
		base.add("id", context.serialize(src.getIdentifier()));
		base.add("type", context.serialize(src.getType().name));
		base.add("value", context.serialize(src.getData().val));
		base.add("mutable", context.serialize(src.getData().mutable));
		return base;
	}

	@Override
	public Field deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		String id = context.deserialize(obj.get("id"), String.class);
		Entry.Type type = Entry.Type.typeFromString(context.deserialize(obj.get("type"), String.class));
		
		// Unknown data type
		if(type == null)
			throw new JsonParseException("Unknown data type '" + obj.get("type").getAsString() + "'.");
		
		Object value = null;
		switch(type) {
			case INTEGER:
				value = context.deserialize(obj.get("value"), Integer.class);
				break;
				
			case DOUBLE:
				value = context.deserialize(obj.get("value"), Double.class);
				break;
			case BOOLEAN:
				value = context.deserialize(obj.get("value"), Boolean.class);
				break;
			case STRING:
				value = context.deserialize(obj.get("value"), String.class);
				break;
			case ENV:
			default:
				break;
		}
		
		// Unknown data type
		if(value == null)
			throw new JsonParseException("Unknown data type '" + obj.get("type").getAsString() + "'.");
		
		boolean mutable = context.deserialize(obj.get("mutable"), boolean.class);
		
		return new Field(id, new Entry(type, value, mutable));
	}

}
