package TIG.scripts.compiler;

/**
 * Matched Token (MToken) is a Token format for after the lexer has matched a token.
 * This contains the Token and the source String that was matched.
 * @author Jacob
 *
 */
public class MToken {
	
	public final Token token;
	public final String match;
	public final int offset;
	public final int length;
	
	public MToken(Token t, String s, int offset, int len) {
		this.token = t;
		this.match = s;
		this.offset = offset;
		this.length = len;
	}
	
	@Override
	public String toString() {
		return token + "('" + match + "')";
	}

}
