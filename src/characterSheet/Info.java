package characterSheet;

import features.Fields;
import utils.JSONUtils;
import utils.JSONify;

/**
 * Basic information needed for a character
 * @author Jacob
 *
 */
public class Info implements JSONify {
	
	private final String playerName;
	private final String characterName;
	
	private final Fields otherData;
	
	/**
	 * Creates character information
	 * @param cname - Character Name
	 * @param pname - Player Name
	 * @param other	- Additional info a character may need (ex: level, exp, race, classes).
	 */
	public Info(String pname, String cname, Fields otherData) {
		this.playerName 	= pname;
		this.characterName 	= cname;
		this.otherData 		= otherData;
	}

	public String getPlayerName() {
		return playerName;
	}

	public String getCharacterName() {
		return characterName;
	}

	public Fields getOtherData() {
		return otherData;
	}

	@Override
	public String toJSON(int indent) {
		String indentString = JSONUtils.getIndent(indent);
		StringBuilder output = new StringBuilder();
		
		output.append("{\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Player Name", this.playerName));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONify("Character Name", this.characterName));
		output.append(",\n");
		output.append(indentString);
		output.append(JSONUtils.basicJSONifyJSON("Other Data", this.otherData.toJSON(indent + 1)));
		output.append("\n");
		output.append(indentString.substring(0, indentString.length() - 1));
		output.append("}");
		
		return output.toString();
	}

}
