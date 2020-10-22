package characterModel.scripts.compiler.parse_tree;

public enum TreeType {
	
	BLOCK,
	SEQ,
	DECLARE,
	DATA,
	IF,
	ELSE,
	WHILE,
	CALL,
	ASSIGN,
	LIST,
	ID,
	LEAF,

	OR_EXPR,
	AND_EXPR,
	EQ_EXPR,
	REL_EXPR,
	ADD_EXPR,
	MUL_EXPR,
	UNARY_EXPR,
	
	ROLL,
	
	EMPTY,
}
