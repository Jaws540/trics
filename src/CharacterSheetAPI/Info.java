package CharacterSheetAPI;

import java.util.Arrays;
import java.util.List;

/**
 * Basic information needed for a character
 * @author Jacob
 *
 */
public class Info {
	
	private final String characterName;
	private final String playerName;
	private final int level;
	private final int exp;
	private final String raceName;
	private final String[] classNames;
	
	private final List<Field<?>> additionalInfo;
	
	/**
	 * Creates character information
	 * @param cname 	 - Character Name
	 * @param pname 	 - Player Name
	 * @param level 	 - Character Level
	 * @param exp 		 - Character Experience
	 * @param rname		 - Race Name
	 * @param classnames - Class Names
	 */
	public Info(String cname, String pname, int level, int exp, String rname, String[] classnames, Field<?>[] other) {
		this.characterName 	= cname;
		this.playerName 	= pname;
		this.level		 	= level;
		this.exp		 	= exp;
		this.raceName	 	= rname;
		this.classNames 	= classnames;
		this.additionalInfo = Arrays.asList(other);
	}

	public String getCharacterName() {
		return characterName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public String getRaceName() {
		return raceName;
	}

	public String[] getClassNames() {
		return classNames;
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
