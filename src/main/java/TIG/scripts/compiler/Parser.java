package TIG.scripts.compiler;

import java.util.List;

import TIG.scripts.compiler.exceptions.SyntaxException;
import TIG.scripts.compiler.parse_tree.Tree;
import TIG.scripts.compiler.parse_tree.TreeType;

public class Parser {
	
	private List<MToken> tokens;
	private int pos = 0;
	
	public Parser(List<MToken> tokens) {
		this.tokens = tokens;
	}
	
	/**
	 * Look ahead in the token list by 1 token
	 * @return The next token in the list
	 */
	private Token lookahead() {
		return tokens.get(pos).token;
	}
	
	/**
	 * Look ahead in the token list by 2 tokens
	 * @return The second token in the list
	 */
	private Token lookahead2() {
		return tokens.get(pos + 1).token;
	}

	/**
	 * Gets the matched text from the Token list of the current Token.
	 * @return A string from the source matching a Token
	 */
	private String getTokenText() {
		return "'" + tokens.get(pos).match + "'";
	}
	
	private int getTokenOffset() {
		return tokens.get(pos).offset;
	}
	
	/**
	 * Matches a Token with an expected Token.  On a successful match, the list pointer is incremented.
	 * @param t - Expected Token
	 * @return The Matched MToken or null if the match failed.
	 */
	private MToken matchToken(Token t) throws SyntaxException {
		if(tokens.get(pos).token == t) {
			pos++;
			return tokens.get(pos - 1);
		}
		
		throw new SyntaxException("Expected " + t + ", got " + getTokenText() + ".", getTokenOffset());
	}
	
	public Tree parse() throws SyntaxException {
		// TODO: Add functions.  They would have to start parsing here
		matchToken(Token.ENTRY);
		return parseBlock();
	}
	
	private Tree parseBlock() throws SyntaxException {
		matchToken(Token.LBRACE);
		Tree tree = parseBlockStatements();
		matchToken(Token.RBRACE);
		return tree;
	}
	
	private Tree parseBlockStatements() throws SyntaxException {
		if(lookahead() == Token.RBRACE) {
			return new Tree(TreeType.EMPTY);
		}
		
		Tree stmt = parseBlockStatement();
		if(lookahead() == Token.RBRACE)
			// Handle a single statement block without a SEQ tree
			return stmt;
		
		Tree rtree = parseBlockStatements();
		if(rtree != null)
			return new Tree(TreeType.SEQ, stmt, rtree);
		return stmt;
	}
	
	private Tree parseBlockStatement() throws SyntaxException {
		switch(lookahead()) {
		case INT:
		case DOUBLE:
		case BOOL:
		case STRING:
			return parseLocalVariableDeclaration();
		default:
			return parseStatement();
		}
	}
	
	private Tree parseLocalVariableDeclaration() throws SyntaxException {
		Tree type = new Tree(TreeType.LEAF, parseType());
		Tree id = new Tree(TreeType.LEAF, matchToken(Token.ID));
		matchToken(Token.ASSIGN);
		Tree expr = parseExpression();
		matchToken(Token.EOL);
		return new Tree(TreeType.DECLARE, new Tree(TreeType.DATA, type, id), expr);
	}
	
	private MToken parseType() throws SyntaxException {
		switch(lookahead()) {
		case INT:
			return matchToken(Token.INT);
		case DOUBLE:
			return matchToken(Token.DOUBLE);
		case BOOL:
			return matchToken(Token.BOOL);
		case STRING:
			return matchToken(Token.STRING);
		default:
			throw new SyntaxException("Expected a type token, got " + getTokenText() + ".", getTokenOffset());
		}
	}
	
	private Tree parseStatement() throws SyntaxException {
		switch(lookahead()) {
		case LBRACE:
			return parseBlock();
		case IF:
			return parseIfStatement();
		case WHILE:
			return parseWhileStatement();
		default:
			return parseLineStatement();
		}
	}
	
	private Tree parseIfStatement() throws SyntaxException {
		matchToken(Token.IF);
		matchToken(Token.LPAREN);
		Tree expr = parseExpression();
		matchToken(Token.RPAREN);
		Tree stmt = parseStatement();
		if(lookahead() == Token.ELSE) {
			matchToken(Token.ELSE);
			Tree elseStmt = parseStatement();
			return new Tree(TreeType.IF, expr, new Tree(TreeType.ELSE, stmt, elseStmt));
		}
		
		return new Tree(TreeType.IF, expr, stmt);
	}
	
	private Tree parseWhileStatement() throws SyntaxException {
		matchToken(Token.WHILE);
		matchToken(Token.LPAREN);
		Tree expr = parseExpression();
		matchToken(Token.RPAREN);
		Tree stmt = parseStatement();
		return new Tree(TreeType.WHILE, expr, stmt);
	}
	
	private Tree parseLineStatement() throws SyntaxException {
		Tree line = parseLine();
		matchToken(Token.EOL);
		return line;
	}
	
	private Tree parseLine() throws SyntaxException {
		if(lookahead2() == Token.LPAREN) {
			return parseCall();
		}else {
			return parseAssignment();
		}
	}
	
	private Tree parseAssignment() throws SyntaxException {
		Tree id = parseIdentifier();
		MToken op = parseAssignmentOperator();
		Tree expr = parseExpression();
		return new Tree(TreeType.ASSIGN, id, expr, op);
	}
	
	private Tree parseIdentifier() throws SyntaxException {
		Tree id = new Tree(TreeType.LEAF, matchToken(Token.ID));
		
		Tree rem = new Tree(TreeType.EMPTY);
		if(lookahead() == Token.DOT) {
			matchToken(Token.DOT);
			rem = parseIdentifier();
		}
		
		return new Tree(TreeType.ID, id, rem);
	}
	
	private MToken parseAssignmentOperator() throws SyntaxException {
		switch(lookahead()) {
		case ASSIGN:
			return matchToken(Token.ASSIGN);
		case PLUS_ASSIGN:
			return matchToken(Token.PLUS_ASSIGN);
		case MINUS_ASSIGN:
			return matchToken(Token.MINUS_ASSIGN);
		case MULT_ASSIGN:
			return matchToken(Token.MULT_ASSIGN);
		case DIV_ASSIGN:
			return matchToken(Token.DIV_ASSIGN);
		case MOD_ASSIGN:
			return matchToken(Token.MOD_ASSIGN);
		default:
			throw new SyntaxException("Expected an assignment operator, got " + getTokenText() + ".", getTokenOffset());
		}
	}
	
	private Tree parseCall() throws SyntaxException {
		Tree id = new Tree(TreeType.LEAF, matchToken(Token.ID));
		matchToken(Token.LPAREN);
		Tree params = parseParameters();
		matchToken(Token.RPAREN);
		return new Tree(TreeType.CALL, id, params);
	}
	
	private Tree parseParameters() throws SyntaxException {
		if(lookahead() == Token.RPAREN) {
			return new Tree(TreeType.EMPTY);
		}
		
		Tree param = parseExpression();
		
		if(lookahead() == Token.COMMA) {
			matchToken(Token.COMMA);
		}
		
		Tree params = parseParameters();
		return new Tree(TreeType.LIST, param, params);
	}
	
	private Tree parseExpression() throws SyntaxException {
		return parseOrExpression();
	}
	
	private Tree parseOrExpression() throws SyntaxException {
		Tree andExpr = parseAndExpression();
		if(lookahead() == Token.OR) {
			MToken or = matchToken(Token.OR);
			Tree orExpr = parseOrExpression();
			return new Tree(TreeType.OR_EXPR, andExpr, orExpr, or);
		}
		
		return andExpr;
	}
	
	private Tree parseAndExpression() throws SyntaxException {
		Tree equalityExpr = parseEqualityExpression();
		if(lookahead() == Token.AND) {
			MToken and = matchToken(Token.AND);
			Tree andExpr = parseAndExpression();
			return new Tree(TreeType.AND_EXPR, equalityExpr, andExpr, and);
		}
		
		return equalityExpr;
	}
	
	private Tree parseEqualityExpression() throws SyntaxException {
		Tree relationExpr = parseRelationExpression();
		Tree equalityExpr;
		switch(lookahead()) {
		case EQUAL:
			MToken eq = matchToken(Token.EQUAL);
			equalityExpr = parseEqualityExpression();
			return new Tree(TreeType.EQ_EXPR, relationExpr, equalityExpr, eq);
		case NOT_EQUAL:
			MToken neq = matchToken(Token.NOT_EQUAL);
			equalityExpr = parseEqualityExpression();
			return new Tree(TreeType.EQ_EXPR, relationExpr, equalityExpr, neq);
		default:
			return relationExpr;
		}
	}
	
	private Tree parseRelationExpression() throws SyntaxException {
		Tree addExpr = parseAddExpression();
		Tree relationExpr;
		switch(lookahead()) {
		case LESS:
			MToken less = matchToken(Token.LESS);
			relationExpr = parseRelationExpression();
			return new Tree(TreeType.REL_EXPR, addExpr, relationExpr, less);
		case GREATER:
			MToken greater = matchToken(Token.GREATER);
			relationExpr = parseRelationExpression();
			return new Tree(TreeType.REL_EXPR, addExpr, relationExpr, greater);
		case LESS_EQUAL:
			MToken less_eq = matchToken(Token.LESS_EQUAL);
			relationExpr = parseRelationExpression();
			return new Tree(TreeType.REL_EXPR, addExpr, relationExpr, less_eq);
		case GREATER_EQUAL:
			MToken greater_eq = matchToken(Token.GREATER_EQUAL);
			relationExpr = parseRelationExpression();
			return new Tree(TreeType.REL_EXPR, addExpr, relationExpr, greater_eq);
		default:
			return addExpr;
		}
	}
	
	private Tree parseAddExpression() throws SyntaxException {
		Tree mulExpr = parseMulExpression();
		Tree addExpr;
		switch(lookahead()) {
		case PLUS:
			MToken plus = matchToken(Token.PLUS);
			addExpr = parseAddExpression();
			return new Tree(TreeType.ADD_EXPR, mulExpr, addExpr, plus);
		case MINUS:
			MToken minus = matchToken(Token.MINUS);
			addExpr = parseAddExpression();
			return new Tree(TreeType.ADD_EXPR, mulExpr, addExpr, minus);
		default:
			return mulExpr;
		}
	}
	
	private Tree parseMulExpression() throws SyntaxException {
		Tree unaryExpr = parseUnaryExpression();
		Tree mulExpr;
		switch(lookahead()) {
		case MULT:
			MToken mul = matchToken(Token.MULT);
			mulExpr = parseMulExpression();
			return new Tree(TreeType.MUL_EXPR, unaryExpr, mulExpr, mul);
		case DIV:
			MToken div = matchToken(Token.DIV);
			mulExpr = parseMulExpression();
			return new Tree(TreeType.MUL_EXPR, unaryExpr, mulExpr, div);
		case MOD:
			MToken mod = matchToken(Token.MOD);
			mulExpr = parseMulExpression();
			return new Tree(TreeType.MUL_EXPR, unaryExpr, mulExpr, mod);
		default:
			return unaryExpr;
		}
	}
	
	private Tree parseUnaryExpression() throws SyntaxException {
		if(lookahead() == Token.NOT) {
			MToken not = matchToken(Token.NOT);
			Tree unaryExpr = parseUnaryExpression();
			return new Tree(TreeType.UNARY_EXPR, new Tree(TreeType.LEAF, not), unaryExpr);
		}
		
		return parsePostfixExpr();
	}
	
	private Tree parsePostfixExpr() throws SyntaxException {
		if(lookahead() == Token.ID) {
			if(lookahead2() == Token.LPAREN) {
				return parseCall();
			}
			
			return parseIdentifier();
		}
		
		//TODO: Add input (check CFG2.txt under <postfix expr>)
		
		return parsePrimary();
	}
	
	private Tree parsePrimary() throws SyntaxException {
		if(lookahead() == Token.LPAREN) {
			matchToken(Token.LPAREN);
			Tree expr = parseExpression();
			matchToken(Token.RPAREN);
			return expr;
		}else if(lookahead2() == Token.DIE) {
			return parseRoll();
		}
		
		return parseLiteral();
	}
	
	private Tree parseLiteral() throws SyntaxException {
		switch(lookahead()) {
		case INT_LITERAL:
			return new Tree(TreeType.LEAF, matchToken(Token.INT_LITERAL));
		case DOUBLE_LITERAL:
			return new Tree(TreeType.LEAF, matchToken(Token.DOUBLE_LITERAL));
		case BOOL_LITERAL:
			return new Tree(TreeType.LEAF, matchToken(Token.BOOL_LITERAL));
		case STRING_LITERAL:
			return new Tree(TreeType.LEAF, matchToken(Token.STRING_LITERAL));
		case NULL:
			return new Tree(TreeType.LEAF, matchToken(Token.NULL));
		default:
			throw new SyntaxException("Expected a type literal, got " + getTokenText() + ".", getTokenOffset());
		}
	}
	
	private Tree parseRoll() throws SyntaxException {
		Tree numDie = new Tree(TreeType.LEAF, matchToken(Token.INT_LITERAL));
		matchToken(Token.DIE);
		Tree numFaces = new Tree(TreeType.LEAF, matchToken(Token.INT_LITERAL));
		return new Tree(TreeType.ROLL, numDie, numFaces);
	}
	
	/*
	 * private Tree parseRoll() throws SyntaxException { Tree simpleRoll =
	 * parseSimpleRoll(); if(lookahead() == Token.PLUS) { MToken add =
	 * matchToken(Token.PLUS); Tree expr = parseExpression(); return new
	 * Tree(TreeType.ADD_EXPR, simpleRoll, expr, add); }else if(lookahead() ==
	 * Token.MINUS) { MToken sub = matchToken(Token.MINUS); Tree expr =
	 * parseExpression(); return new Tree(TreeType.ADD_EXPR, simpleRoll, expr, sub);
	 * }
	 * 
	 * return simpleRoll; }
	 * 
	 * private Tree parseSimpleRoll() throws SyntaxException { Tree numDie = new
	 * Tree(TreeType.LEAF, matchToken(Token.INT_LITERAL)); matchToken(Token.DIE);
	 * Tree numFaces = new Tree(TreeType.LEAF, matchToken(Token.INT_LITERAL));
	 * return new Tree(TreeType.ROLL, numDie, numFaces); }
	 */
	
}
