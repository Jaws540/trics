package characterModel.utils.gsonAdapters;

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

import characterModel.features.Feature;
import characterModel.features.Features;

public class FeaturesSerializer implements JsonSerializer<Features>, JsonDeserializer<Features> {

	@Override
	public JsonElement serialize(Features src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.getFeats());
	}

	@Override
	public Features deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonArray features = json.getAsJsonArray();
		
		List<Feature> featureList = new ArrayList<>();
		for(JsonElement j : features) {
			Feature feat = context.deserialize(j, Feature.class);
			featureList.add(feat);
		}
		
		return new Features(featureList.toArray(new Feature[featureList.size()]));
	}

}
