package TIG.utils.gsonAdapters;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import TIG.characterSheet.CharacterSheet;
import TIG.characterSheet.Info;
import TIG.characterSheet.Inventory;
import TIG.features.Features;
import TIG.notes.Notes;

public class CharacterSheetSerializer implements JsonSerializer<CharacterSheet>, JsonDeserializer<CharacterSheet> {

	@Override
	public JsonElement serialize(CharacterSheet src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject base = new JsonObject();
		base.add("info", context.serialize(src.info));
		base.add("features", context.serialize(src.features));
		base.add("inventory", context.serialize(src.inventory));
		base.add("notes", context.serialize(src.notes));
		return base;
	}

	@Override
	public CharacterSheet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		Info info = context.deserialize(obj.get("info"), Info.class);
		Features feats = context.deserialize(obj.get("features"), Features.class);
		Inventory inv = context.deserialize(obj.get("inventory"), Inventory.class);
		Notes notes = context.deserialize(obj.get("notes"), Notes.class);
		return new CharacterSheet(info, feats, inv, notes);
	}

}
