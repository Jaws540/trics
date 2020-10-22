package characterModel.scripts.compiler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import characterModel.scripts.compiler.parse_tree.Tree;
import characterModel.scripts.compiler.parse_tree.TreeType;
import characterModel.utils.exceptions.compileExceptions.SyntaxException;

public class Parser {
	
	private static final Logger LOG = LoggerFactory.getLogger(Parser.class);
	
	private List<MToken> tokens;
	private int pos = 0;
	
	public Parser(List<MToken> tokens) {
		LOG.debug("Initialized parser");
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
			LOG.trace("Matched " + t.toString());
			pos++;
			return tokens.get(pos - 1);
		}
		
		throw new SyntaxException("Expected " + t + ", got " + getTokenText() + ".", getTokenOffset());
	}
	
	// TODO: Add functions.
	
	public Tree parse() throws SyntaxException {
		LOG.debug("Parsing");
		matchToken(Token.ENTRY);
		return parseBlock();
	}
	
	private Tree parseBlock() throws SyntaxException {
		matchToken(Token.LBRACE);
		Tree tree = parseBlockStatements();
		matchToken(Token.RBRACE);
		return new Tree(TreeType.BLOCK, tree, null);
	}
	
	private Tree parseBlockStatements() throws SyntaxException {
			LOG.trace("Parsing empty tree");
		if(lookahead() == Token.RBRACE) {
			return new Tree(TreeType.EMPTY);
		}
		
		Tree stmt = parseStatement();
		if(lookahead() == Token.RBRACE)
			// Handle a single statement block without a SEQ tree
			return stmt;
		
		Tree rtree = parseBlockStatements();
		if(rtree != null) {
			return new Tree(TreeType.SEQ, stmt, rtree);
		}
		return stmt;
	}
	
	/*
	
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
		//Tree type = new Tree(TreeType.LEAF, parseType());
		Tree id = new Tree(TreeType.LEAF, matchToken(Token.ID));
		matchToken(Token.ASSIGN);
		Tree expr = parseExpression();
		matchToken(Token.EOL);
		//return new Tree(TreeType.DECLARE, new Tree(TreeType.DATA, type, id), expr);
		return new Tree(TreeType.DECLARE, id, expr);
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
	*/
	
	private Tree parseStatement() throws SyntaxException {
		switch(lookahead()) {
			case LBRACE:
				return parseBlock();
			case IF:
				return parseIfStatement();
			case WHILE:
				return parseWhileStatement();
			default:
				return parseLine();
		}
	}
	
	private Tree parseIfStatement() throws SyntaxException {
		LOG.trace("Parsing if");
		matchToken(Token.IF);
		matchToken(Token.LPAREN);
		Tree expr = parseExpression();
		matchToken(Token.RPAREN);
		Tree stmt = parseStatement();
		if(lookahead() == Token.ELSE) {
			matchToken(Token.ELSE);
			Tree elseStmt = parseStatement();
			LOG.trace("Parsed if-else tree");
			return new Tree(TreeType.IF, expr, new Tree(TreeType.ELSE, stmt, elseStmt));
		}
		
		LOG.trace("Parsed if tree");
		return new Tree(TreeType.IF, expr, stmt);
	}
	
	private Tree parseWhileStatement() throws SyntaxException {
		LOG.trace("Parsing while");
		matchToken(Token.WHILE);
		matchToken(Token.LPAREN);
		Tree expr = parseExpression();
		matchToken(Token.RPAREN);
		Tree stmt = parseStatement();
		LOG.trace("Parsed while tree");
		return new Tree(TreeType.WHILE, expr, stmt);
	}
	
	/*
	 * Removed the need for semi-colons
	private Tree parseLineStatement() throws SyntaxException {
		Tree line = parseLine();
		matchToken(Token.EOL);
		return line;
	}
	*/
	
	private Tree parseLine() throws SyntaxException {
		if(lookahead2() == Token.LPAREN) {
			return parseCall();
		}else {
			return parseAssignment();
		}
	}
	
	private Tree parseAssignment() throws SyntaxException {
		LOG.trace("Parsing assignment");
		Tree id = parseIdentifier();
		MToken op = parseAssignmentOperator();
		Tree expr = parseExpression();
		return new Tree(TreeType.ASSIGN, id, expr, op);
	}
	
	private Tree parseIdentifier() throws SyntaxException {
		LOG.trace("Parsing ID");
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
		LOG.trace("Parsing call");
		Tree id = new Tree(TreeType.LEAF, matchToken(Token.ID));
		matchToken(Token.LPAREN);
		Tree params = parseParameters();
		matchToken(Token.RPAREN);
		return new Tree(TreeType.CALL, id, params);
	}
	
	private Tree parseParameters() throws SyntaxException {
		LOG.trace("Parsing call parameters");
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
			LOG.trace("Parsing or expression");
			MToken or = matchToken(Token.OR);
			Tree orExpr = parseOrExpression();
			return new Tree(TreeType.OR_EXPR, andExpr, orExpr, or);
		}
		
		return andExpr;
	}
	
	private Tree parseAndExpression() throws SyntaxException {
		Tree equalityExpr = parseEqualityExpression();
		if(lookahead() == Token.AND) {
			LOG.trace("Parsing and expression");
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
				LOG.trace("Parsing equals expression");
				MToken eq = matchToken(Token.EQUAL);
				equalityExpr = parseEqualityExpression();
				return new Tree(TreeType.EQ_EXPR, relationExpr, equalityExpr, eq);
			case NOT_EQUAL:
				LOG.trace("Parsing not-equals expression");
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
				LOG.trace("Parsing less than expression");
				MToken less = matchToken(Token.LESS);
				relationExpr = parseRelationExpression();
				return new Tree(TreeType.REL_EXPR, addExpr, relationExpr, less);
			case GREATER:
				LOG.trace("Parsing greater than expression");
				MToken greater = matchToken(Token.GREATER);
				relationExpr = parseRelationExpression();
				return new Tree(TreeType.REL_EXPR, addExpr, relationExpr, greater);
			case LESS_EQUAL:
				LOG.trace("Parsing less than or equal expression");
				MToken less_eq = matchToken(Token.LESS_EQUAL);
				relationExpr = parseRelationExpression();
				return new Tree(TreeType.REL_EXPR, addExpr, relationExpr, less_eq);
			case GREATER_EQUAL:
				LOG.trace("Parsing greater than or equal expression");
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
				LOG.trace("Parsing addition expression");
				MToken plus = matchToken(Token.PLUS);
				addExpr = parseAddExpression();
				return new Tree(TreeType.ADD_EXPR, mulExpr, addExpr, plus);
			case MINUS:
				LOG.trace("Parsing subtraction expression");
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
				LOG.trace("Parsing multiplication expression");
				MToken mul = matchToken(Token.MULT);
				mulExpr = parseMulExpression();
				return new Tree(TreeType.MUL_EXPR, unaryExpr, mulExpr, mul);
			case DIV:
				LOG.trace("Parsing division expression");
				MToken div = matchToken(Token.DIV);
				mulExpr = parseMulExpression();
				return new Tree(TreeType.MUL_EXPR, unaryExpr, mulExpr, div);
			case MOD:
				LOG.trace("Parsing modulus expression");
				MToken mod = matchToken(Token.MOD);
				mulExpr = parseMulExpression();
				return new Tree(TreeType.MUL_EXPR, unaryExpr, mulExpr, mod);
			default:
				return unaryExpr;
		}
	}
	
	private Tree parseUnaryExpression() throws SyntaxException {
		if(lookahead() == Token.NOT) {
			LOG.trace("Parsing not expression");
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
		LOG.trace("Parsing literal expression");
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
		LOG.trace("Parsing roll expression");
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
