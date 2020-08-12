package TIG.utils;

import java.util.UUID;

import TIG.characterSheet.CharacterSheet;
import TIG.characterSheet.Info;
import TIG.characterSheet.Inventory;
import TIG.features.Feature;
import TIG.features.Features;
import TIG.features.Field;
import TIG.features.Fields;
import TIG.items.Item;
import TIG.notes.Note;
import TIG.notes.NoteTree;
import TIG.notes.Notes;
import TIG.tags.Tag;

public class Test {
	
	public static void main(String[] args) {
		// INFO
		Field<?>[] otherInfo = {
			new Field<Integer>("Level", 6),
			new Field<Integer>("EXP", 25000),
			new Field<String>("Race", "Dragonborn"),
			new Field<String>("Classes", "Fighter"),
		};
		Info characterInfo = new Info("Illra", "Jacob", new Fields(otherInfo));
		
		// FEATURES
		Tag[] abilityTags = {
			new Tag("Ability score"),
			new Tag("Modifier")
		};
		
		Field<?>[] strFields = {
			new Field<Integer>("Value", 18),
			new Field<Integer>("Modifier", 4)
		};
		Field<?>[] dexFields = {
			new Field<Integer>("Value", 18),
			new Field<Integer>("Modifier", 4)
		};
		Field<?>[] conFields = {
			new Field<Integer>("Value", 18),
			new Field<Integer>("Modifier", 4)
		};
		Field<?>[] intFields = {
			new Field<Integer>("Value", 18),
			new Field<Integer>("Modifier", 4)
		};
		Field<?>[] wisFields = {
			new Field<Integer>("Value", 18),
			new Field<Integer>("Modifier", 4)
		};
		Field<?>[] chaFields = {
			new Field<Integer>("Value", 18),
			new Field<Integer>("Modifier", 4)
		};
		Feature[] featsList = {
			new Feature("Strength", "Strength Ability", new Fields(strFields), null, abilityTags),
			new Feature("Dexterity", "Dexterity Ability", new Fields(dexFields), null, abilityTags),
			new Feature("Constitution", "Constitution Ability", new Fields(conFields), null, abilityTags),
			new Feature("Intelligence", "Intelligence Ability", new Fields(intFields), null, abilityTags),
			new Feature("Wisdom", "Wisdom Ability", new Fields(wisFields), null, abilityTags),
			new Feature("Charisma", "Charisma Ability", new Fields(chaFields), null, abilityTags),
		};
		Features feats = new Features(featsList);
		
		// INVENTORY
		Item[] items = {
			new Item(UUID.randomUUID(), "Dagger", "Stab with it lol", 1, 1, null),
			new Item(UUID.randomUUID(), "Sword", "Stab with it from farther away lol", 15, 10, null),
			new Item(UUID.randomUUID(), "Potion of Healing", "Get them life points back", 0.5, 20, null, true),
			new Item(UUID.randomUUID(), "Potion of Healing", "Get them life points back", 0.5, 20, null, true),
		};
		Field<?>[] inventoryBaseFields = {
			new Field<Integer>("Carry Weight", 250),
			new Field<Integer>("Current Weight", 0)
		};
		Feature[] inventoryFeatList = {
			new Feature("Base", "Basic inventory feature.", new Fields(inventoryBaseFields), null, null)
		};
		Features inventoryFeats = new Features(inventoryFeatList);
		Inventory inventory = new Inventory(items, inventoryFeats);
		
		// NOTES
		Notes notes = new Notes();
		NoteTree sessionNotes = new NoteTree("Session Notes");
		sessionNotes.addNote(new Note("Session 1", "It twas a long and grueling session, but I think I will prevail!"));
		sessionNotes.addNote(new Note("Session 2", "It twas a long and grueling session, but I think I will prevail!"));
		notes.addNote(new Note("Something", "Lol, idk, I just needed something to test the JSON formatting with"));
		
		CharacterSheet character = new CharacterSheet(characterInfo, feats, inventory, notes);
		System.out.println(Utils.saveJSON(character, "D:\\Users\\Jacob\\Coding\\Java\\RPGIS\\RPG-Integrated-System\\res\\testSave1.json"));
	}

}
