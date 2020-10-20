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

import TIG.items.Item;
import TIG.items.Items;

public class ItemsSerializer implements JsonSerializer<Items>, JsonDeserializer<Items> {

	@Override
	public JsonElement serialize(Items src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getItems());
	}

	@Override
	public Items deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray jsonArr = json.getAsJsonArray();
		
		List<Item> items = new ArrayList<>();
		for(JsonElement j : jsonArr) {
			Item item = context.deserialize(j, Item.class);
			items.add(item);
		}
		
		return new Items(items.toArray(new Item[items.size()]));
	}

}
