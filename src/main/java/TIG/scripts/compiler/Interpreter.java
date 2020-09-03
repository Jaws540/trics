package TIG.scripts.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import TIG.scripts.Def;
import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.compileExceptions.CompileException;
import TIG.scripts.compiler.exceptions.compileExceptions.ExceptionList;
import TIG.scripts.compiler.exceptions.interpreterExceptions.ExistenceException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.InterpreterRuntimeException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.InvalidArgumentsException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.InvalidOperationException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.TypeException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.UndefinedFunctionException;
import TIG.scripts.compiler.exceptions.interpreterExceptions.UnretrievableException;
import TIG.scripts.compiler.parse_tree.Tree;
import TIG.scripts.compiler.parse_tree.TreeType;
import TIG.utils.Utils;

/**
 * Runs script source files
 * @author Jacob
 *
 */
public class Interpreter {
	
	private final String src;
	private final Environment env;
	
	private Tree ast;
	private int pos = 0;
	
	/**
	 * Create a new Interpreter.
	 * @param srcPath - Relative path to the source file
	 * @param env - The Environment containing relavant data
	 */
	public Interpreter(String srcPath, Environment env) {
		// Read the source file as a String
		this.src = Utils.loadScript(srcPath);
		this.env = env;
	}
	
	/**
	 * "Compiles" the source file into an interpretable form.  This will pass any errors onto the ErrorHandler.
	 * @return <code>true</code> on a successful compile, false otherwise.
	 */
	public boolean compile() {
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
	
	public boolean run() {
		if(ast != null) {
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
			case DECLARE:
				interpretDeclare(tree, mem);
				break;
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
	
	private void interpretDeclare(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		pos = tree.token.offset;
		
	}
	
	private void interpretIf(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		Entry exprResult = interpretExpression(tree.left, mem);
		if(exprResult.type == Entry.Type.BOOL) {
			boolean result = ((Boolean) exprResult.val).booleanValue();
			if(tree.right.type == TreeType.ELSE) {
				if(result)
					interpret(tree.right.left, mem);
				else
					interpret(tree.right.right, mem);
			}else if(result) {
				interpret(tree.right, mem);
			}
		}else {
			throw new TypeException(pos, "Expected bool.");
		}
	}
	
	private void interpretWhile(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		boolean result = false;
		do {
			Entry exprResult = interpretExpression(tree.left, mem);
			if(exprResult.type == Entry.Type.BOOL) {
				result = ((Boolean) exprResult.val).booleanValue();
				if(result) {
					interpret(tree.right, mem);
				}
			}else {
				throw new TypeException(pos, "Expected bool.");
			}
		} while(result);
	}
	
	private Entry interpretCall(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		pos = tree.left.token.offset;
		Entry[] args = interpretList(tree.right, mem);
		switch(tree.left.token.match) {
			case Def.DISPLAY:
				if(args.length == 1) {
					interpretDisplay(args[0], mem);
					return new Entry(Entry.Type.BOOL, true);
				}else {
					throw new InvalidArgumentsException("Display has one string parameter.", pos);
				}
			default:
				throw new UndefinedFunctionException(tree.left.token.match, pos);
		}
	}
	
	private void interpretDisplay(Entry arg, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		String output = "";
		switch(arg.type) {
			case INT:
			case DOUBLE:
			case BOOL:
				output = "" + arg.val;
				break;
			case STRING:
				output = (String) arg.val;
				break;
			default:
				throw new TypeException(pos, "Unknown type given.");
		}
		
		System.out.println(output);
	}
	
	private Entry[] interpretList(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
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
		pos = tree.token.offset;
		
		Entry id = interpretIDExpression(tree.left, mem);
		Entry value = interpretExpression(tree.right, mem);
		if(id == null && tree.token.token == Token.ASSIGN) {
			mem.put(interpretLeaf(tree.left.left), value);
		}else if(id == null && tree.token.token != Token.ASSIGN) {
			throw new InvalidOperationException(pos);
		}else if(id.type == value.type) {
			switch(tree.token.token) {
				case PLUS_ASSIGN:
					if(id.type == Entry.Type.BOOL)
						throw new TypeException(pos, "Undefined for type bool.");
					
					value = interpretExpression(refactorMathAssign(TreeType.ADD_EXPR, tree.left, tree.right, Token.PLUS, tree.token), mem);
					break;
				case MINUS_ASSIGN:
					if(id.type == Entry.Type.BOOL)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING)
						throw new TypeException(pos, "Non-addition math is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.ADD_EXPR, tree.left, tree.right, Token.MINUS, tree.token), mem);
					break;
				case MULT_ASSIGN:
					if(id.type == Entry.Type.BOOL)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING)
						throw new TypeException(pos, "Non-addition math is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.MUL_EXPR, tree.left, tree.right, Token.MULT, tree.token), mem);
					break;
				case DIV_ASSIGN:
					if(id.type == Entry.Type.BOOL)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING)
						throw new TypeException(pos, "Non-addition math is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.MUL_EXPR, tree.left, tree.right, Token.DIV, tree.token), mem);
					break;
				case MOD_ASSIGN:
					if(id.type == Entry.Type.BOOL)
						throw new TypeException(pos, "Undefined for type bool.");
					if(id.type == Entry.Type.STRING)
						throw new TypeException(pos, "Non-addition math is undefined for type string.");
					
					value = interpretExpression(refactorMathAssign(TreeType.MUL_EXPR, tree.left, tree.right, Token.MOD, tree.token), mem);
					break;
				default:
					break;
			}
			
			id.val = value.val;
		}else {
			throw new TypeException(pos, "Expected type " + id.type.name + ".");
		}
	}
	
	private Tree refactorMathAssign(TreeType type, Tree left, Tree right, Token op, MToken copy) {
		return new Tree(type, left, right, new MToken(op, copy.match, copy.offset, copy.length));
	}
	
	private Entry interpretExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
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
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == Entry.Type.BOOL && right.type == Entry.Type.BOOL) {
			return new Entry(Entry.Type.BOOL, ((Boolean) left.val) || ((Boolean) right.val));
		}
		
		throw new TypeException(pos, "Expected bool.");
	}
	
	private Entry interpretAndExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == Entry.Type.BOOL && right.type == Entry.Type.BOOL) {
			return new Entry(Entry.Type.BOOL, ((Boolean) left.val) && ((Boolean) right.val));
		}
		
		throw new TypeException(pos, "Expected bool.");
	}
	
	private Entry interpretEqualityExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == right.type) {
			switch(left.type) {
				case INT:
					return new Entry(Entry.Type.BOOL, ((Integer) left.val) == ((Integer) right.val));
				case DOUBLE:
					return new Entry(Entry.Type.BOOL, ((Double) left.val) == ((Double) right.val));
				case BOOL:
					return new Entry(Entry.Type.BOOL, ((Boolean) left.val) == ((Boolean) right.val));
				case STRING:
					return new Entry(Entry.Type.BOOL, ((String) left.val).equals((String) right.val));
				default:
					throw new InvalidOperationException(pos);
			}
		}
		
		throw new TypeException(pos, "Expected " + left.type.name + ".");
	}
	
	private Entry interpretRelationalExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		Token op = tree.token.token;
		// Defined for all base types except boolean
		if(left.type == right.type) {
			switch(left.type) {
				case INT:
					return new Entry(Entry.Type.BOOL, interpretRelation((Integer) left.val, (Integer) right.val, op));
				case DOUBLE:
					return new Entry(Entry.Type.BOOL, interpretRelation((Double) left.val, (Double) right.val, op));
				case BOOL:
					throw new TypeException(pos, "Relational expressions are undefined for type bool.");
				case STRING:
					return new Entry(Entry.Type.BOOL, interpretRelation((String) left.val, (String) right.val, op));
				default:
					throw new InvalidOperationException(pos);
			}
			
		// Also defined for comparissons between numerical types (i.e. integers and doubles are comparable)
		}else if (left.type == Entry.Type.INT && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.BOOL, interpretRelation((Integer) left.val, (Double) right.val, op));
		}else if (left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INT) {
			return new Entry(Entry.Type.BOOL, interpretRelation((Double) left.val, (Integer) right.val, op));
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
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		Token op = tree.token.token;
		if(left.type == right.type) {
			switch(left.type) {
				case INT:
					return new Entry(Entry.Type.INT, interpretAdd((Integer) left.val, (Integer) right.val, op));
				case DOUBLE:
					return new Entry(Entry.Type.DOUBLE, interpretAdd((Double) left.val, (Double) right.val, op));
				case BOOL:
					throw new TypeException(pos, "Math is undefined for type bool.");
				case STRING:
					if(op == Token.PLUS)
						return new Entry(Entry.Type.STRING, ((String) left.val) + ((String) right.val));
					else
						throw new TypeException(pos, "Non-addition math is undefined for type string.");
				default:
					throw new InvalidOperationException(pos);
			}
		}else if(left.type == Entry.Type.INT && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.DOUBLE, interpretAdd((Integer) left.val, (Double) right.val, op));
		}else if(left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INT) {
			return new Entry(Entry.Type.DOUBLE, interpretAdd((Double) left.val, (Integer) right.val, op));
		}else if(left.type == Entry.Type.STRING && op == Token.PLUS) {
			switch(right.type) {
				case INT:
					return new Entry(Entry.Type.STRING, ((String) left.val) + ((Integer) right.val).intValue());
				case DOUBLE:
					return new Entry(Entry.Type.STRING, ((String) left.val) + ((Double) right.val).doubleValue());
				case BOOL:
					return new Entry(Entry.Type.STRING, ((String) left.val) + ((Boolean) right.val).booleanValue());
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
		pos = tree.token.offset;
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		Token op = tree.token.token;
		if(left.type == right.type) {
			switch(left.type) {
			case INT:
				return new Entry(Entry.Type.INT, interpretMultiply((Integer) left.val, (Integer) right.val, op));
			case DOUBLE:
				return new Entry(Entry.Type.DOUBLE, interpretMultiply((Double) left.val, (Double) right.val, op));
			case BOOL:
				throw new TypeException(pos, "Math is undefined for type bool.");
			case STRING:
				throw new TypeException(pos, "Non-addition math is undefined for type string.");
			default:
				throw new InvalidOperationException(pos);
			}
		}else if(left.type == Entry.Type.INT && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.DOUBLE, interpretMultiply((Integer) left.val, (Double) right.val, op));
		}else if(left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INT) {
			return new Entry(Entry.Type.DOUBLE, interpretMultiply((Double) left.val, (Integer) right.val, op));
		}else if(left.type == Entry.Type.STRING && right.type == Entry.Type.INT) {
			// Python-like string multiplication
			String out = "";
			for(int i = 0; i < (Integer) right.val; i++) 
				out += (String) left.val;
			return new Entry(Entry.Type.STRING, out);
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
		pos = tree.token.offset;
		if(tree.left.type == TreeType.LEAF && tree.left.token.token == Token.NOT) {
			Entry right = interpretExpression(tree.right, mem);
			if(right.type == Entry.Type.BOOL) {
				return new Entry(Entry.Type.BOOL, !((Boolean) right.val));
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
		Entry var = mem.get(interpretLeaf(tree.left));
		return var;
	}
	
	private Entry interpretIDExpressionHelper(Tree tree, HashMap<String, Entry> mem, Environment env) throws InterpreterRuntimeException {
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
		}
		
		return null;
	}
	
	private Entry interpretLiteralExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		pos = tree.token.offset;
		if(tree.type == TreeType.LEAF) {
			String data = interpretLeaf(tree);
			switch(tree.token.token) {
				case INT_LITERAL:
					return new Entry(Entry.Type.INT, Integer.parseInt(data));
				case DOUBLE_LITERAL:
					return new Entry(Entry.Type.DOUBLE, Double.parseDouble(data));
				case BOOL_LITERAL:
					return new Entry(Entry.Type.BOOL, Boolean.parseBoolean(data));
				case STRING_LITERAL:
					return new Entry(Entry.Type.STRING, data);
				default:
					throw new TypeException(pos, "Unknown literal type.");
			}
		}
		
		throw new InvalidOperationException(pos);
	}
	
	// TODO: Implement custom dice rolling.  Probably through a 'standard library' call to roll()?
	private Entry interpretRollExpression(Tree tree, HashMap<String, Entry> mem) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left, mem);
		Entry right = interpretExpression(tree.right, mem);
		if(left.type == Entry.Type.INT && right.type == Entry.Type.INT) {
			int numDie = ((Integer) left.val).intValue();
			int numFaces = ((Integer) right.val).intValue();
			int sum = 0;
			Random rand = new Random();
			for(int i = 0; i < numDie; i++) {
				sum += rand.nextInt(numFaces) + 1;
			}
			return new Entry(Entry.Type.INT, sum);
		}
		
		throw new TypeException(pos, "Expected <int>d<int>.");
	}
	
	private String interpretLeaf(Tree tree) throws InterpreterRuntimeException {
		pos = tree.token.offset;
		return tree.token.match;
	}
	
	private void interpretEmpty(Tree tree) {}

}
