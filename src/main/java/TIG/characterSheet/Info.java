package TIG.characterSheet;

import TIG.features.Fields;
import TIG.scripts.Def;
import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.interpreterExceptions.ImmutableException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.InterpreterRuntimeException;

/**
 * Basic information needed for a character
 * @author Jacob
 *
 */
public class Info implements Environment {
	
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
	public Entry envGet(String identifier, int pos) throws InterpreterRuntimeException {
		switch(identifier) {
			case Def.PLAYER_NAME:
				return new Entry(Entry.Type.STRING, playerName);
			case Def.CHARACTER_NAME:
				return new Entry(Entry.Type.STRING, characterName);
			default:
				return otherData.envGet(identifier, pos);
		}
	}

	@Override
	public boolean envPut(String identifier, Entry obj, int pos) throws InterpreterRuntimeException {
		switch(identifier) {
		case Def.PLAYER_NAME:
		case Def.CHARACTER_NAME:
			throw new ImmutableException(pos);
		default:
			return otherData.envPut(identifier, obj, pos);
	}
	}

}
