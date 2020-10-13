package TIG.scripts.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.parse_tree.Tree;
import TIG.scripts.compiler.parse_tree.TreeType;
import TIG.utils.Def;
import TIG.utils.IO;
import TIG.utils.exceptions.compileExceptions.CompileException;
import TIG.utils.exceptions.compileExceptions.ExceptionList;
import TIG.utils.exceptions.interpreterExceptions.ExistenceException;
import TIG.utils.exceptions.interpreterExceptions.ImmutableException;
import TIG.utils.exceptions.interpreterExceptions.InterpreterRuntimeException;
import TIG.utils.exceptions.interpreterExceptions.InvalidArgumentsException;
import TIG.utils.exceptions.interpreterExceptions.InvalidOperationException;
import TIG.utils.exceptions.interpreterExceptions.TypeException;
import TIG.utils.exceptions.interpreterExceptions.UndefinedFunctionException;
import TIG.utils.exceptions.interpreterExceptions.UnretrievableException;

/**
 * Runs script source files
 * @author Jacob
 *
 */
public class Interpreter {
	
	private static final Logger LOG = LoggerFactory.getLogger(Interpreter.class);
	
	private final String src;
	private Environment env;
	
	private Tree ast;
	private int pos = 0;
	
	/**
	 * Create a new Interpreter.
	 * @param srcPath - Relative path to the source file
	 * @param env - The Environment containing relavant data
	 */
	public Interpreter(String rawSrc) {
		// Set the source
		this.src = rawSrc;
	}
	
	/**
	 * "Compiles" the source file into an interpretable form.  This will pass any errors onto the ErrorHandler.
	 * @return <code>true</code> on a successful compile, false otherwise.
	 */
	public boolean compile() {
		LOG.debug("Compiling script");
		if(src != null) {
			// Attempt to interpret the source file
			try {
				List<MToken> tokens = Lexer.lex(src);
				this.ast = new Parser(tokens).parse();
				return true;
			} catch (ExceptionList e) {
				ErrorHandler.handleExceptionList(e, src);
			} catch(CompileException e) {
				ErrorHandler.handleCompileException(e, src);
			}
		}
		
		return false;
	}
	
	public boolean run(Environment env) {
		LOG.debug("Running script");
		if(ast != null) {
			this.env = env;
			try {
				pos = 0;
				HashMap<String, Entry> memory = new HashMap<String, Entry>();
				interpret(ast, memory);
				return true;
			} catch (InterpreterRuntimeException e) {
				ErrorHandler.handleInterpreterException(e, src);
			} catch (Exception e) {
				ErrorHandler.handleUnknown(pos, src);
				e.printStackTrace();
			}
		}else {
			LOG.error("Script was not compiled!  The script must be compiled before being run.");
		}
		
		return false;
	}
	
	private HashMap<String, Entry> closure(HashMap<String, Entry> mem) {
		return new HashMap<String, Entry>(mem);
	}
	
	private void interpret(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		switch(tree.type) {
			case BLOCK:
				interpret(tree.left, closure(mem));
				break;
			case SEQ:
				interpretSequence(tree, mem);
				break;
				/*
			case DECLARE:
				interpretDeclare(tree, mem);
				break;
				*/
			case IF:
				interpretIf(tree, mem);
				break;
			case WHILE:
				interpretWhile(tree, mem);
				break;
			case CALL:
				interpretCall(tree, mem);
				break;
			case ASSIGN:
				interpretAssign(tree, mem);
				break;
			case EMPTY:
				interpretEmpty(tree);
				break;
			default:
				throw new InvalidOperationException(pos);
		}
	}

	private void interpretSequence(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		interpret(tree.left, mem);
		interpret(tree.right, mem);
	}
	
	/*
	private void interpretDeclare(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting declaration");
		pos = tree.token.offset;
	}
	*/
	
	private void interpretIf(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting if statement");
		Entry exprResult = interpretExpression(tree.left, mem);
		if(exprResult.type == Entry.Type.BOOLEAN) {
			boolean result = ((Boolean) exprResult.val).booleanValue();
			if(tree.right.type == TreeType.ELSE) {
				if(result) {
					LOG.trace("Interpreting if statement true");
					interpret(tree.right.left, mem);
				}else {
					LOG.trace("Interpreting if statement else");
					interpret(tree.right.right, mem);
				}
			}else if(result) {
				LOG.trace("Interpreting if statement true");
				interpret(tree.right, mem);
			}
		}else {
			throw new TypeException(pos, "Expected bool.");
		}
	}
	
	private void interpretWhile(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting while loop");
		boolean result = false;
		do {
			Entry exprResult = interpretExpression(tree.left, mem);
			if(exprResult.type == Entry.Type.BOOLEAN) {
				result = ((Boolean) exprResult.val).booleanValue();
				if(result) {
					LOG.trace("Interpreting while loop true");
					interpret(tree.right, mem);
				}
			}else {
				throw new TypeException(pos, "Expected bool.");
			}
		} while(result);
	}
	
	private Entry interpretCall(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting call statement");
		pos = tree.left.token.offset;
		Entry[] args = interpretList(tree.right, mem);
		switch(tree.left.token.match) {
			case Def.DISPLAY:
				if(args.length == 1) {
					StdLibrary.interpretDisplay(args[0], mem, pos);
					return new Entry(Entry.Type.BOOLEAN, true, false);
				}else {
					throw new InvalidArgumentsException("Display has one string parameter.", pos);
				}
			default:
				throw new UndefinedFunctionException(tree.left.token.match, pos);
		}
	}
	
	private Entry[] interpretList(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting list");
		if(tree.type == TreeType.EMPTY) {
			return new Entry[0];
		}
		
		Entry first = interpretExpression(tree.left, mem);
		Entry[] rem = interpretList(tree.right, mem);
		
		List<Entry> args = new ArrayList<>();
		args.add(first);
		for(Entry e : rem) {
			args.add(e);
		}
		
		return args.toArray(new Entry[args.size()]);
	}
	
	private void interpretAssign(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting assignment statement");
		pos = tree.token.offset;
		
		Entry id = interpretIDExpression(tree.left, mem);
		Entry value = interpretExpression(tree.right, mem); // This returns an immutable entry
		if(id == null && tree.token.token == Token.ASSIGN) {
			LOG.trace("Creating local variable");
			// Ensure a local variable is mutable
			mem.put(interpretLeaf(tree.left.left), new Entry(value.type, value.val, true));
		}else if(id == null && tree.token.token != Token.ASSIGN) {
			// Can't do math on a non-existant variable;
			throw new ExistenceException(pos);
		}else if(id.type == value.type) {
			switch(tree.token.token) {
				case PLUS_ASSIGN:
					LOG.trace("Interpreting plus-assign");
					if(id.type == Entry.Type.BOOLEAN)
						throw new TypeException(pos, "Undefined for type bool.");
					
					value = interpretExpression(refactorMathAssign(TreeType.ADD_EXPR, tree.left, tree.right, Token.PLUS, tree.token), mem);
					break;
				case MINUS_ASSIGN:
					LOG.trace("Interpreting minus-assign");
					if(id.type == Entry.Type.BOOLEAN)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING)
						throw new TypeException(pos, "Non-addition math/non-integer multiplication is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.ADD_EXPR, tree.left, tree.right, Token.MINUS, tree.token), mem);
					break;
				case MULT_ASSIGN:
					LOG.trace("Interpreting mult-assign");
					if(id.type == Entry.Type.BOOLEAN)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING && value.type != Entry.Type.INTEGER)
						throw new TypeException(pos, "Non-addition math/non-integer multiplication is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.MUL_EXPR, tree.left, tree.right, Token.MULT, tree.token), mem);
					break;
				case DIV_ASSIGN:
					LOG.trace("Interpreting div-assign");
					if(id.type == Entry.Type.BOOLEAN)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING)
						throw new TypeException(pos, "Non-addition math/non-integer multiplication is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.MUL_EXPR, tree.left, tree.right, Token.DIV, tree.token), mem);
					break;
				case MOD_ASSIGN:
					LOG.trace("Interpreting mod-assign");
					if(id.type == Entry.Type.BOOLEAN)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING)
						throw new TypeException(pos, "Non-addition math/non-integer multiplication is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.MUL_EXPR, tree.left, tree.right, Token.MOD, tree.token), mem);
					break;
				default:
					throw new InvalidOperationException(pos);
			}
			
			if(id.mutable)
				id.val = value.val;
			else
				throw new ImmutableException(pos);
		}else {
			throw new TypeException(pos, "Expected type " + id.type.name + ".");
		}
	}
	
	private Tree refactorMathAssign(TreeType type, Tree left, Tree right, Token op, MToken copy) {
		return new Tree(type, left, right, new MToken(op, copy.match, copy.offset, copy.length));
	}
	
	private Entry interpretExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting expression");
		switch(tree.type) {
			case OR_EXPR:
				return interpretOrExpression(tree, mem);
			case AND_EXPR:
				return interpretAndExpression(tree, mem);
			case EQ_EXPR:
				return interpretEqualityExpression(tree, mem);
			case REL_EXPR:
				return interpretRelationalExpression(tree, mem);
			case ADD_EXPR:
				return interpretAddExpression(tree, mem);
			case MUL_EXPR:
				return interpretMultiplyExpression(tree, mem);
			case UNARY_EXPR:
				return interpretUnaryExpression(tree, mem);
			default:
				return interpretPostfixExpression(tree, mem);
		}
	}
	
	private Entry interpretOrExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting or expression");
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == Entry.Type.BOOLEAN && right.type == Entry.Type.BOOLEAN) {
			return new Entry(Entry.Type.BOOLEAN, ((Boolean) left.val) || ((Boolean) right.val), false);
		}
		
		throw new TypeException(pos, "Expected bool.");
	}
	
	private Entry interpretAndExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting and expression");
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == Entry.Type.BOOLEAN && right.type == Entry.Type.BOOLEAN) {
			return new Entry(Entry.Type.BOOLEAN, ((Boolean) left.val) && ((Boolean) right.val), false);
		}
		
		throw new TypeException(pos, "Expected bool.");
	}
	
	private Entry interpretEqualityExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting equality expression");
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == right.type) {
			switch(left.type) {
				case INTEGER:
					return new Entry(Entry.Type.BOOLEAN, ((Integer) left.val) == ((Integer) right.val), false);
				case DOUBLE:
					return new Entry(Entry.Type.BOOLEAN, ((Double) left.val) == ((Double) right.val), false);
				case BOOLEAN:
					return new Entry(Entry.Type.BOOLEAN, ((Boolean) left.val) == ((Boolean) right.val), false);
				case STRING:
					return new Entry(Entry.Type.BOOLEAN, ((String) left.val).equals((String) right.val), false);
				default:
					throw new InvalidOperationException(pos);
			}
		}
		
		throw new TypeException(pos, "Expected " + left.type.name + ".");
	}
	
	private Entry interpretRelationalExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting relational expression");
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		Token op = tree.token.token;
		// Defined for all base types except boolean
		if(left.type == right.type) {
			switch(left.type) {
				case INTEGER:
					return new Entry(Entry.Type.BOOLEAN, interpretRelation((Integer) left.val, (Integer) right.val, op), false);
				case DOUBLE:
					return new Entry(Entry.Type.BOOLEAN, interpretRelation((Double) left.val, (Double) right.val, op), false);
				case BOOLEAN:
					throw new TypeException(pos, "Relational expressions are undefined for type bool.");
				case STRING:
					return new Entry(Entry.Type.BOOLEAN, interpretRelation((String) left.val, (String) right.val, op), false);
				default:
					throw new InvalidOperationException(pos);
			}
			
		// Also defined for comparissons between numerical types (i.e. integers and doubles are comparable)
		}else if (left.type == Entry.Type.INTEGER && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.BOOLEAN, interpretRelation((Integer) left.val, (Double) right.val, op), false);
		}else if (left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INTEGER) {
			return new Entry(Entry.Type.BOOLEAN, interpretRelation((Double) left.val, (Integer) right.val, op), false);
		}
		
		throw new TypeException(pos, "Undefined relation for " + left.type.name + " and " + right.type.name + ".");
	}
	
	private boolean interpretRelation(Integer left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case LESS:
				return left < right;
			case GREATER:
				return left > right;
			case LESS_EQUAL:
				return left <= right;
			case GREATER_EQUAL:
				return left >= right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private boolean interpretRelation(Double left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case LESS:
				return left < right;
			case GREATER:
				return left > right;
			case LESS_EQUAL:
				return left <= right;
			case GREATER_EQUAL:
				return left >= right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private boolean interpretRelation(Integer left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case LESS:
				return left < right;
			case GREATER:
				return left > right;
			case LESS_EQUAL:
				return left <= right;
			case GREATER_EQUAL:
				return left >= right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private boolean interpretRelation(Double left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case LESS:
				return left < right;
			case GREATER:
				return left > right;
			case LESS_EQUAL:
				return left <= right;
			case GREATER_EQUAL:
				return left >= right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private boolean interpretRelation(String left, String right, Token op) throws InterpreterRuntimeException {
		int out = left.compareTo(right);
		switch(op) {
			case LESS:
				return out < 0;
			case GREATER:
				return out > 0;
			case LESS_EQUAL:
				return out <= 0;
			case GREATER_EQUAL:
				return out >= 0;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private Entry interpretAddExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting add expression");
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		Token op = tree.token.token;
		if(left.type == right.type) {
			switch(left.type) {
				case INTEGER:
					return new Entry(Entry.Type.INTEGER, interpretAdd((Integer) left.val, (Integer) right.val, op), false);
				case DOUBLE:
					return new Entry(Entry.Type.DOUBLE, interpretAdd((Double) left.val, (Double) right.val, op), false);
				case BOOLEAN:
					throw new TypeException(pos, "Math is undefined for type bool.");
				case STRING:
					if(op == Token.PLUS)
						return new Entry(Entry.Type.STRING, ((String) left.val) + ((String) right.val), false);
					else
						throw new TypeException(pos, "Non-addition math is undefined for type string.");
				default:
					throw new InvalidOperationException(pos);
			}
		}else if(left.type == Entry.Type.INTEGER && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.DOUBLE, interpretAdd((Integer) left.val, (Double) right.val, op), false);
		}else if(left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INTEGER) {
			return new Entry(Entry.Type.DOUBLE, interpretAdd((Double) left.val, (Integer) right.val, op), false);
		}else if(left.type == Entry.Type.STRING && op == Token.PLUS) {
			switch(right.type) {
				case INTEGER:
					return new Entry(Entry.Type.STRING, ((String) left.val) + ((Integer) right.val).intValue(), false);
				case DOUBLE:
					return new Entry(Entry.Type.STRING, ((String) left.val) + ((Double) right.val).doubleValue(), false);
				case BOOLEAN:
					return new Entry(Entry.Type.STRING, ((String) left.val) + ((Boolean) right.val).booleanValue(), false);
				default:
					throw new InvalidOperationException(pos);
			}
		}
		
		throw new TypeException(pos, "Addition is undefined for " + left.type.name + " and " + right.type.name + ".");
	}
	
	private int interpretAdd(Integer left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left + right;
			case MINUS:
				return left - right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private double interpretAdd(Double left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left + right;
			case MINUS:
				return left - right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private double interpretAdd(Integer left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left.doubleValue() + right;
			case MINUS:
				return left.doubleValue() - right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private double interpretAdd(Double left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left + right.doubleValue();
			case MINUS:
				return left - right.doubleValue();
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private Entry interpretMultiplyExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting multiply expression");
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		Token op = tree.token.token;
		if(left.type == right.type) {
			switch(left.type) {
			case INTEGER:
				return new Entry(Entry.Type.INTEGER, interpretMultiply((Integer) left.val, (Integer) right.val, op), false);
			case DOUBLE:
				return new Entry(Entry.Type.DOUBLE, interpretMultiply((Double) left.val, (Double) right.val, op), false);
			case BOOLEAN:
				throw new TypeException(pos, "Math is undefined for type bool.");
			case STRING:
				throw new TypeException(pos, "Non-addition math is undefined for type string.");
			default:
				throw new InvalidOperationException(pos);
			}
		}else if(left.type == Entry.Type.INTEGER && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.DOUBLE, interpretMultiply((Integer) left.val, (Double) right.val, op), false);
		}else if(left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INTEGER) {
			return new Entry(Entry.Type.DOUBLE, interpretMultiply((Double) left.val, (Integer) right.val, op), false);
		}else if(left.type == Entry.Type.STRING && right.type == Entry.Type.INTEGER) {
			// Python-like string multiplication
			String out = "";
			for(int i = 0; i < (Integer) right.val; i++) 
				out += (String) left.val;
			return new Entry(Entry.Type.STRING, out, false);
		}
		
		throw new TypeException(pos, "Addition is undefined for " + left.type.name + " and " + right.type.name + ".");
	}
	
	private int interpretMultiply(Integer left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case MULT:
				return left * right;
			case DIV:
				return left - right;
			case MOD:
				return left % right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private double interpretMultiply(Double left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case MULT:
				return left * right;
			case DIV:
				return left - right;
			case MOD:
				return left % right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private double interpretMultiply(Integer left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case MULT:
				return left.doubleValue() * right;
			case DIV:
				return left.doubleValue() - right;
			case MOD:
				return left.doubleValue() % right;
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private double interpretMultiply(Double left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case MULT:
				return left * right.doubleValue();
			case DIV:
				return left - right.doubleValue();
			case MOD:
				return left % right.doubleValue();
			default:
				throw new InvalidOperationException(pos);
		}
	}
	
	private Entry interpretUnaryExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting not expression");
		pos = tree.token.offset;
		if(tree.left.type == TreeType.LEAF && tree.left.token.token == Token.NOT) {
			Entry right = interpretExpression(tree.right, mem);
			if(right.type == Entry.Type.BOOLEAN) {
				return new Entry(Entry.Type.BOOLEAN, !((Boolean) right.val), false);
			}
			
			throw new TypeException(pos, "Expected bool.");
		}
		
		throw new InvalidOperationException(pos);
	}
	
	private Entry interpretPostfixExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		switch(tree.type) {
		case CALL:
			return interpretCall(tree, mem);
		case ID:
			// This needs to catch null IDs so they don't try to be resolved to a value
			Entry id = interpretIDExpression(tree, mem);
			if(id == null)
				throw new ExistenceException(pos);
			return id;
		case ROLL:
			return interpretRollExpression(tree, mem);
		default:
			return interpretLiteralExpression(tree, mem);
		}
	}
	
	private Entry interpretIDExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		// Has to be able to fail without throwing an existance exception to get to the local variable lookup
		Entry id = interpretIDExpressionHelper(tree, mem, this.env);
		if(id != null && id.type != Entry.Type.ENV)
			return id;
		
		// Local variable
		String idName = interpretLeaf(tree.left);
		LOG.trace("Retrieving ID '" + idName + "' from memory");
		Entry var = mem.get(idName);
		return var;
	}
	
	private Entry interpretIDExpressionHelper(Tree tree, HashMap<String, Entry> mem, Environment env) throws InterpreterRuntimeException {
		LOG.trace("Resolving ID '" + tree.left.token.match + "'");
		if(tree.type == TreeType.EMPTY)
			throw new UnretrievableException(pos);
		
		if(env != null) {
			String id = interpretLeaf(tree.left);
			Entry res = null;
			try {
				res = env.envGet(id, pos);
			
				if(res.type == Entry.Type.ENV)
					return interpretIDExpressionHelper(tree.right, mem, (Environment) res.val);
			}catch(ExistenceException e) {}
			
			return res;
		}else {
			// No environment was given when running the interpreter so no environment IDs are resolvable
			throw new ExistenceException(pos);
		}
	}
	
	private Entry interpretLiteralExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting literal expression");
		pos = tree.token.offset;
		if(tree.type == TreeType.LEAF) {
			String data = interpretLeaf(tree);
			switch(tree.token.token) {
				case INT_LITERAL:
					return new Entry(Entry.Type.INTEGER, Integer.parseInt(data), false);
				case DOUBLE_LITERAL:
					return new Entry(Entry.Type.DOUBLE, Double.parseDouble(data), false);
				case BOOL_LITERAL:
					return new Entry(Entry.Type.BOOLEAN, Boolean.parseBoolean(data), false);
				case STRING_LITERAL:
					return new Entry(Entry.Type.STRING, data, false);
				default:
					throw new TypeException(pos, "Unknown literal type.");
			}
		}
		
		throw new InvalidOperationException(pos);
	}
	
	// TODO: Implement custom dice rolling.  Probably through a 'standard library' call to roll()?
	private Entry interpretRollExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		LOG.trace("Interpreting roll expression");
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == Entry.Type.INTEGER && right.type == Entry.Type.INTEGER) {
			int numDie = ((Integer) left.val).intValue();
			int numFaces = ((Integer) right.val).intValue();
			int sum = 0;
			Random rand = new Random();
			for(int i = 0; i < numDie; i++) {
				sum += rand.nextInt(numFaces) + 1;
			}
			return new Entry(Entry.Type.INTEGER, sum, false);
		}
		
		throw new TypeException(pos, "Expected <int>'d'<int>.");
	}
	
	private String interpretLeaf(Tree tree) throws InterpreterRuntimeException {
		pos = tree.token.offset;
		return tree.token.match;
	}
	
	private void interpretEmpty(Tree tree) {}

}
