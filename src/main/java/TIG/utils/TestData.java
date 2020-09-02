package TIG.utils;

import java.util.Date;

import TIG.characterSheet.CharacterSheet;
import TIG.characterSheet.Info;
import TIG.characterSheet.Inventory;
import TIG.features.Feature;
import TIG.features.Features;
import TIG.features.Field;
import TIG.features.Fields;
import TIG.items.Item;
import TIG.items.Items;
import TIG.notes.Note;
import TIG.notes.NoteTree;
import TIG.notes.Notes;
import TIG.tags.Tag;

public class TestData {
	
	public static CharacterSheet testCharacter;
	static {
		try {
			// INFO
			String[] classList = {"Fighter", "Wizard"};
			Field<?>[] otherInfo = {
				new Field<Integer>("Level", 6, Field.Type.INT),
				new Field<Integer>("EXP", 25000, Field.Type.INT),
				new Field<String>("Race", "Dragonborn", Field.Type.STRING),
				new Field<String[]>("Classes", classList, Field.Type.INT),
			};
			Info characterInfo = new Info("Illra", "Jacob", new Fields(otherInfo));
			
			// FEATURES
			Tag[] abilityTags = {
				new Tag("Ability score"),
				new Tag("Modifier")
			};
			
			Field<?>[] strFields = {
				new Field<Integer>("Value", 18, Field.Type.INT),
				new Field<Integer>("Modifier", 4, Field.Type.INT)
			};
			Field<?>[] dexFields = {
				new Field<Integer>("Value", 18, Field.Type.INT),
				new Field<Integer>("Modifier", 4, Field.Type.INT)
			};
			Field<?>[] conFields = {
				new Field<Integer>("Value", 18, Field.Type.INT),
				new Field<Integer>("Modifier", 4, Field.Type.INT)
			};
			Field<?>[] intFields = {
				new Field<Integer>("Value", 18, Field.Type.INT),
				new Field<Integer>("Modifier", 4, Field.Type.INT)
			};
			Field<?>[] wisFields = {
				new Field<Integer>("Value", 18, Field.Type.INT),
				new Field<Integer>("Modifier", 4, Field.Type.INT)
			};
			Field<?>[] chaFields = {
				new Field<Integer>("Value", 18, Field.Type.INT),
				new Field<Integer>("Modifier", 4, Field.Type.INT)
			};
			Feature[] featsList = {
				new Feature("Strength", "Strength", "Strength Ability", new Fields(strFields), null, abilityTags),
				new Feature("Dexterity", "Dexterity", "Dexterity Ability", new Fields(dexFields), null, abilityTags),
				new Feature("Constitution", "Constitution", "Constitution Ability", new Fields(conFields), null, abilityTags),
				new Feature("Intelligence", "Intelligence", "Intelligence Ability", new Fields(intFields), null, abilityTags),
				new Feature("Wisdom", "Wisdom", "Wisdom Ability", new Fields(wisFields), null, abilityTags),
				new Feature("Charisma", "Charisma", "Charisma Ability", new Fields(chaFields), null, abilityTags),
			};
			Features feats = new Features(featsList);
			
			// INVENTORY
			Item[] itemList = {
				new Item("dagger", "Dagger", "Stab with it lol", 1, 1, 1, null),
				new Item("sword", "Sword", "Stab with it from farther away lol", 15, 10, 1, null),
				new Item("potion_of_healing", "Potion of Healing", "Get them life points back", 0.5, 20, 3, null),
			};
			Field<?>[] inventoryBaseFields = {
				new Field<Integer>("Carry_Weight", 250, Field.Type.INT),
				new Field<Integer>("Current_Weight", 0, Field.Type.INT),
			};
			Feature[] inventoryFeatList = {
				new Feature("Base_Inventory", "Base", "Basic inventory feature.", new Fields(inventoryBaseFields), null, null),
			};
			Items items = new Items(itemList);
			Features inventoryFeats = new Features(inventoryFeatList);
			Inventory inventory = new Inventory(items, inventoryFeats);
			
			// NOTES
			Notes notes = new Notes();
			
			notes.addNote(new Note("Something", "Lol, idk, I just needed something to test the JSON formatting with", new Date()));
			
			NoteTree sessionNotes = new NoteTree("Session Notes");
			sessionNotes.addNote(new Note("Session 1", "It t'was a long and grueling session, but I think I will prevail!", new Date()));
			sessionNotes.addNote(new Note("Session 2", "It t'was a long and grueling session, but I think I will prevail!", new Date()));
			notes.addFolder(sessionNotes);
			
			testCharacter = new CharacterSheet(characterInfo, feats, inventory, notes);
		} catch(Exception e) {
			Log.fatal("Could not initialize test data!  Check field names/tag texts for invalid strings.");
		}
	}

}
