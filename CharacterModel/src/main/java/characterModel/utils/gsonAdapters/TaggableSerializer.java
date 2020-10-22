package characterModel.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import characterModel.tags.Taggable;

public class TaggableSerializer implements JsonSerializer<Taggable>/*, JsonDeserializer<Taggable> */{

	@Override
	public JsonElement serialize(Taggable src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getTags());
	}

	/*
	@Override
	public Taggable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray tags = json.getAsJsonArray();
		
		List<Tag> tagList = new ArrayList<>();
		for(JsonElement j : tags) {
			Tag feat = context.deserialize(j, Tag.class);
			tagList.add(feat);
		}
		
		return new Taggable(tagList.toArray(new Tag[tagList.size()]));
	}
	*/

}
