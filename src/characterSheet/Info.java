package characterSheet;

import java.util.Arrays;
import java.util.List;

import characterSheet.features.Field;

/**
 * Basic information needed for a character
 * @author Jacob
 *
 */
public class Info {
	
	private final String characterName;
	private final String playerName;
	
	private final List<Field<?>> additionalInfo;
	
	/**
	 * Creates character information
	 * @param cname - Character Name
	 * @param pname - Player Name
	 * @param other	- Additional info a character may need (ex: level, exp, race, classes).
	 */
	public Info(String cname, String pname, Field<?>[] other) {
		this.characterName 	= cname;
		this.playerName 	= pname;
		this.additionalInfo = Arrays.asList(other);
	}

	public String getCharacterName() {
		return characterName;
	}

	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Gets the names of all the Fields in the additional info list
	 * @return A String array of names
	 */
	public String[] getFieldNames() {
		String[] names = new String[this.additionalInfo.size()];
		for(int i = 0; i < this.additionalInfo.size(); i++) {
			names[i] = this.additionalInfo.get(i).getIdentifier();
		}
		return names;
	}
	
	/**
	 * Gets a specific Field object
	 * @param identifier - The name of the Field
	 * @return The Field object (Can be edited)
	 */
	public Field<?> getField(String identifier) {
		for(Field<?> f : this.additionalInfo) {
			if(f.getIdentifier().equals(identifier))
				return f;
		}
		return null;
	}

}
