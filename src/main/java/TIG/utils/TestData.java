package TIG.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import TIG.scripts.Entry;
import TIG.tags.Tag;

public class TestData {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestData.class);
	
	public static CharacterSheet testCharacter;
	static {
		try {
			// INFO
			Field[] otherInfo = {
				new Field("Level", new Entry(Entry.Type.INTEGER, 6, true)),
				new Field("EXP", new Entry(Entry.Type.INTEGER, 25000, true)),
				new Field("Race", new Entry(Entry.Type.STRING, "Dragonborn", true)),
				new Field("Class1", new Entry(Entry.Type.STRING, "Fighter", true)),
				new Field("Class2", new Entry(Entry.Type.STRING, "Wizard", true)),
			};
			Info characterInfo = new Info("Jacob", "Illra", new Fields(otherInfo));
			
			// FEATURES
			Tag[] abilityTags = {
				new Tag("Ability score"),
				new Tag("Modifier")
			};
			
			Field[] strFields = {
				new Field("Value", new Entry(Entry.Type.INTEGER, 18, true)),
				new Field("Modifier", new Entry(Entry.Type.INTEGER, 4, true))
			};
			Field[] dexFields = {
				new Field("Value", new Entry(Entry.Type.INTEGER, 18, true)),
				new Field("Modifier", new Entry(Entry.Type.INTEGER, 4, true))
			};
			Field[] conFields = {
				new Field("Value", new Entry(Entry.Type.INTEGER, 18, true)),
				new Field("Modifier", new Entry(Entry.Type.INTEGER, 4, true))
			};
			Field[] intFields = {
				new Field("Value", new Entry(Entry.Type.INTEGER, 18, true)),
				new Field("Modifier", new Entry(Entry.Type.INTEGER, 4, true))
			};
			Field[] wisFields = {
				new Field("Value", new Entry(Entry.Type.INTEGER, 18, true)),
				new Field("Modifier", new Entry(Entry.Type.INTEGER, 4, true))
			};
			Field[] chaFields = {
				new Field("Value", new Entry(Entry.Type.INTEGER, 18, true)),
				new Field("Modifier", new Entry(Entry.Type.INTEGER, 4, true))
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
				new Item("$dagger", "Dagger", "Stab with it lol", 1, 1, 1, null),
				new Item("sword", "Sword", "Stab with it from farther away lol", 15, 10, 1, null),
				new Item("potion_of_healing", "Potion of Healing", "Get them life points back", 0.5, 20, 3, null),
			};
			Field[] inventoryBaseFields = {
				new Field("Carry_Weight", new Entry(Entry.Type.DOUBLE, 250.0, true)),
				new Field("Current_Weight", new Entry(Entry.Type.DOUBLE, 0.0, true)),
			};
			Feature[] inventoryFeatList = {
				new Feature("dnd::inventory", "Base", "Basic inventory feature.", new Fields(inventoryBaseFields), null, null),
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
			LOG.error("Could not initialize test data");
		}
	}

}
