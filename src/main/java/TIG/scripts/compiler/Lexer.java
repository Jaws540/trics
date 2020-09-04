package TIG.scripts.compiler;

import java.util.ArrayList;
import java.util.List;

import TIG.utils.exceptions.compileExceptions.ExceptionList;
import TIG.utils.exceptions.compileExceptions.LexerException;

public class Lexer {
	
	/**
	 * Marches through a script source string to find all its tokens
	 * @param source - Script source string
	 * @return A list of tokens found sequentially in source
	 */
	public static List<MToken> lex(String source) throws ExceptionList {
		List<LexerException> exceptions = new ArrayList<>();
		
		// Token accumulator
		List<MToken> tokens = new ArrayList<MToken>();
		
		// March though source string character by character
		int pos = 0;
		while(pos < source.length()) {
			// Get the current substring
			String sub = source.substring(pos);
			
			// Find the Token that consumes the most characters
			int longestCount = 0;
			MToken longestMatch = null;
			boolean whitespace = false;
			for(Token t : Token.values()) {
				
				// If the token matches the substring
				int len = t.matches(sub); // Length of the match
				
				if(len > 0) {
					// Select this token if it is not whitespace and is the longest match
					if(len > longestCount) {
						if(t != Token.WHITESPACE && t != Token.COMMENT) {
							if(t == Token.STRING_LITERAL) {
								longestMatch = new MToken(t, sub.substring(1, len - 1), pos, len);
							}else {
								longestMatch = new MToken(t, sub.substring(0, len), pos, len);
							}
						}else {
							whitespace = true;
						}
						longestCount = len;
					}
				}
			}
			
			if(longestMatch != null) {
				tokens.add(longestMatch);
			}else if(whitespace != true) {
				exceptions.add(new LexerException(pos));
			}
			
			// Move past the Token
			if(longestCount > 0)
				pos += longestCount;
			else
				// No Token was found, move forward 1 (there should be an error cast)
				pos++;
		}
		
		if(exceptions.size() > 0) {
			throw new ExceptionList(exceptions);
		}
		
		return tokens;
	}

}
