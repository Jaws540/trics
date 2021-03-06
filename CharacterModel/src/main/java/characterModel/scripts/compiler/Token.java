package characterModel.scripts.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import characterModel.utils.Def;

public enum Token {
	// Regex characters needing to be escaped: .[]^-&$?*+,{}|():=!<>
	
	// Control
	ENTRY("script"),
	//QUERY("\\?"),
	IF("if"),
	ELSE("else"),
	WHILE("while"),
	DOT("\\.", "."),
	LPAREN("\\(", "("),
	RPAREN("\\)", ")"),
	LBRACE("\\{", "{"),
	RBRACE("\\}", "}"),
	NULL("null"),
	// Semi-colons as line endings are not needed!
	//EOL(";"),
	COMMA("\\,", ","),
	
	// Logic
	OR("or"),
	AND("and"),
	EQUAL("\\=\\=", "=="),
	NOT_EQUAL("\\!\\=", "!="),
	LESS("\\<", "<"),
	GREATER("\\>", ">"),
	LESS_EQUAL("\\<\\=", "<="),
	GREATER_EQUAL("\\>\\=", ">="),
	NOT("not"),
	
	// Assignment
	ASSIGN("\\=", "="),
	PLUS_ASSIGN("\\+\\=", "+="),
	MINUS_ASSIGN("\\-\\=", "-="),
	MULT_ASSIGN("\\*\\=", "*="),
	DIV_ASSIGN("/\\=", "/="),
	MOD_ASSIGN("%\\=", "%="),
	
	// Arithmetic
	PLUS("\\+", "+"),
	MINUS("\\-", "-"),
	MULT("\\*", "*"),
	DIV("/"),
	MOD("%"),
	
	// Die Rolls
	DIE("'d'"),
	
	// Literals
	INT_LITERAL("\\-?\\d+", "integer literal"),
	DOUBLE_LITERAL("\\-?\\d+\\.\\d+", "double literal"),
	BOOL_LITERAL("(true|false)", "bool literal"),
	STRING_LITERAL("\"(\\\\\"|[^\"])*\"", "string literal"), // A sequence of characters that are not the " character surrounded by "s

	// IDs
	ID(Def.ID_REGEX, "identifier"),
	
	// Whitespace
	WHITESPACE("\\s+", "whitespace"),
	
	// Comment
	COMMENT("#[^\\n]*\n");
	
	private Pattern pattern;
	private String name;
	
	private Token(String reg){
		pattern = Pattern.compile(reg);
		this.name = reg;
	}
	
	private Token(String reg, String name){
		pattern = Pattern.compile(reg);
		this.name = name;
	}
	
	public int matches(String input) {
		Matcher matcher = pattern.matcher(input);
		
		if(matcher.find() && matcher.start() == 0) {
			return matcher.end();
		}else {
			return -1;
		}
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	 

}
