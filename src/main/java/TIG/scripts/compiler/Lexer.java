package TIG.scripts.compiler;

import java.util.ArrayList;
import java.util.List;

import TIG.utils.exceptions.compileExceptions.CompileException;
import TIG.utils.exceptions.compileExceptions.ExceptionList;
import TIG.utils.exceptions.compileExceptions.InvalidEscapeSequenceException;
import TIG.utils.exceptions.compileExceptions.UnknownSymbolException;

public class Lexer {
	
	/**
	 * Marches through a script source string to find all its tokens
	 * @param source - Script source string
	 * @return A list of tokens found sequentially in source
	 */
	public static List<MToken> lex(String source) throws ExceptionList {
		List<CompileException> exceptions = new ArrayList<>();
		
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
								try {
									String escaped = escapeCharacters(sub.substring(1, len - 1), pos);
									longestMatch = new MToken(t, escaped, pos, len);
								} catch (InvalidEscapeSequenceException e) {
									exceptions.add(e);
									// Put in a dummy token so the lexer doesn't throw an unknown symbol exception
									longestMatch = new MToken(t, "", pos, len);
								}
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
				exceptions.add(new UnknownSymbolException(pos));
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
	
	/**
	 * Handles character escaping
	 * @param raw - Direct string literal from the source code
	 * @return A String that has any escape sequences translated into java understandable form
	 */
	private static String escapeCharacters(String raw, int srcOffset) throws InvalidEscapeSequenceException {
		// return raw.replaceAll("\\\\\"", "\"").replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t");
		StringBuilder str = new StringBuilder();
		int pos = 0;
		while(pos < raw.length()) {
			char curr = raw.charAt(pos);
			if(curr == '\\') {
				pos++;
				char next = raw.charAt(pos);
				switch(next) {
					case '\\':
						str.append('\\');
						break;
					case '\"':	
						str.append('\"');
						break;
					case 'n':
						str.append('\n');
						break;
					case 't':
						str.append('\t');
						break;
					default:
						throw new InvalidEscapeSequenceException(srcOffset + pos);
				}
			}else {
				str.append(curr);
			}
			
			pos++;
		}
		
		return str.toString();
	}

}
