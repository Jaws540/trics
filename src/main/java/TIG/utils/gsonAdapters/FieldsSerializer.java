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

import TIG.features.Field;
import TIG.features.Fields;

public class FieldsSerializer implements JsonSerializer<Fields>, JsonDeserializer<Fields> {

	@Override
	public JsonElement serialize(Fields src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getFields());
	}

	@Override
	public Fields deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray jsonArr = json.getAsJsonArray();
		
		List<Field> fields = new ArrayList<>();
		for(JsonElement j : jsonArr) {
			Field field = context.deserialize(j, Field.class);
			fields.add(field);
		}
		
		return new Fields(fields.toArray(new Field[fields.size()]));
	}

}
