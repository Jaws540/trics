package TIG.scripts.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import TIG.scripts.Def;
import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.CompileException;
import TIG.scripts.compiler.exceptions.ExceptionList;
import TIG.scripts.compiler.exceptions.ExistenceException;
import TIG.scripts.compiler.exceptions.InterpreterRuntimeException;
import TIG.scripts.compiler.exceptions.InvalidArgumentsException;
import TIG.scripts.compiler.exceptions.InvalidOperationException;
import TIG.scripts.compiler.exceptions.TypeException;
import TIG.scripts.compiler.exceptions.UndefinedFunctionException;
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
	
	private HashMap<String, Entry> memory = null;
	
	private Tree ast;
	
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
				memory = new HashMap<>();
				interpret(ast);
				return true;
			} catch (InterpreterRuntimeException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private void interpret(Tree tree) throws InterpreterRuntimeException {
		switch(tree.type) {
			case SEQ:
				interpretSequence(tree);
				break;
			case DECLARE:
				interpretDeclare(tree);
				break;
			case IF:
				interpretIf(tree);
				break;
			case WHILE:
				interpretWhile(tree);
				break;
			case CALL:
				interpretCall(tree);
				break;
			case ASSIGN:
				interpretAssign(tree);
				break;
			case EMPTY:
				interpretEmpty(tree);
				break;
			default:
				throw new InvalidOperationException();
		}
	}

	private void interpretSequence(Tree tree) throws InterpreterRuntimeException {
		interpret(tree.left);
		interpret(tree.right);
	}
	
	private void interpretDeclare(Tree tree) throws InterpreterRuntimeException {
		
	}
	
	private void interpretIf(Tree tree) throws InterpreterRuntimeException {
		Entry exprResult = interpretExpression(tree.left);
		if(exprResult.type == Entry.Type.BOOL) {
			boolean result = ((Boolean) exprResult.val).booleanValue();
			if(tree.right.type == TreeType.ELSE) {
				if(result)
					interpret(tree.right.left);
				else
					interpret(tree.right.right);
			}else if(result) {
				interpret(tree.right);
			}
		}else {
			throw new TypeException();
		}
	}
	
	private void interpretWhile(Tree tree) throws InterpreterRuntimeException {
		boolean result = false;
		do {
			Entry exprResult = interpretExpression(tree.left);
			if(exprResult.type == Entry.Type.BOOL) {
				result = ((Boolean) exprResult.val).booleanValue();
				if(result) {
					interpret(tree.right);
				}
			}else {
				throw new TypeException();
			}
		} while(result);
	}
	
	private Entry interpretCall(Tree tree) throws InterpreterRuntimeException {
		Entry[] args = interpretList(tree.right);
		switch(tree.left.token.match) {
			case Def.DISPLAY:
				if(args.length == 1) {
					interpretDisplay(args[0]);
					return new Entry(Entry.Type.BOOL, true);
				}else {
					throw new InvalidArgumentsException("Display accepts 1 string argument.");
				}
			default:
				throw new UndefinedFunctionException(tree.left.token.match);
		}
	}
	
	private void interpretDisplay(Entry arg) throws InterpreterRuntimeException {
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
				throw new TypeException();
		}
		
		System.out.println(output);
	}
	
	private Entry[] interpretList(Tree tree) throws InterpreterRuntimeException {
		if(tree.type == TreeType.EMPTY) {
			return new Entry[0];
		}
		
		Entry first = interpretExpression(tree.left);
		Entry[] rem = interpretList(tree.right);
		
		List<Entry> args = new ArrayList<>();
		args.add(first);
		for(Entry e : rem) {
			args.add(e);
		}
		
		return args.toArray(new Entry[args.size()]);
	}
	
	private void interpretAssign(Tree tree) throws InterpreterRuntimeException {
		
	}
	
	private Entry interpretExpression(Tree tree) throws InterpreterRuntimeException {
		switch(tree.type) {
			case OR_EXPR:
				return interpretOrExpression(tree);
			case AND_EXPR:
				return interpretAndExpression(tree);
			case EQ_EXPR:
				return interpretEqualityExpression(tree);
			case REL_EXPR:
				return interpretRelationalExpression(tree);
			case ADD_EXPR:
				return interpretAddExpression(tree);
			case MUL_EXPR:
				return interpretMultiplyExpression(tree);
			case UNARY_EXPR:
				return interpretUnaryExpression(tree);
			default:
				return interpretPostfixExpression(tree);
		}
	}
	
	private Entry interpretOrExpression(Tree tree) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left);
		Entry right = interpretExpression(tree.right);
		if(left.type == Entry.Type.BOOL && right.type == Entry.Type.BOOL) {
			return new Entry(Entry.Type.BOOL, ((Boolean) left.val) || ((Boolean) right.val));
		}
		
		throw new TypeException();
	}
	
	private Entry interpretAndExpression(Tree tree) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left);
		Entry right = interpretExpression(tree.right);
		if(left.type == Entry.Type.BOOL && right.type == Entry.Type.BOOL) {
			return new Entry(Entry.Type.BOOL, ((Boolean) left.val) && ((Boolean) right.val));
		}
		
		throw new TypeException();
	}
	
	private Entry interpretEqualityExpression(Tree tree) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left);
		Entry right = interpretExpression(tree.right);
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
					throw new InvalidOperationException();
			}
		}
		
		throw new TypeException();
	}
	
	private Entry interpretRelationalExpression(Tree tree) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left);
		Entry right = interpretExpression(tree.right);
		Token op = tree.token.token;
		// Defined for all base types except boolean
		if(left.type == right.type) {
			switch(left.type) {
				case INT:
					return new Entry(Entry.Type.BOOL, interpretRelation((Integer) left.val, (Integer) right.val, op));
				case DOUBLE:
					return new Entry(Entry.Type.BOOL, interpretRelation((Double) left.val, (Double) right.val, op));
				case BOOL:
					throw new TypeException();
				case STRING:
					return new Entry(Entry.Type.BOOL, interpretRelation((String) left.val, (String) right.val, op));
				default:
					throw new InvalidOperationException();
			}
			
		// Also defined for comparissons between numerical types (i.e. integers and doubles are comparable)
		}else if (left.type == Entry.Type.INT && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.BOOL, interpretRelation((Integer) left.val, (Double) right.val, op));
		}else if (left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INT) {
			return new Entry(Entry.Type.BOOL, interpretRelation((Double) left.val, (Integer) right.val, op));
		}
		
		throw new TypeException();
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
				throw new InvalidOperationException();
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
				throw new InvalidOperationException();
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
				throw new InvalidOperationException();
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
				throw new InvalidOperationException();
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
				throw new InvalidOperationException();
		}
	}
	
	private Entry interpretAddExpression(Tree tree) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left);
		Entry right = interpretExpression(tree.right);
		Token op = tree.token.token;
		if(left.type == right.type) {
			switch(left.type) {
				case INT:
					return new Entry(Entry.Type.INT, interpretAdd((Integer) left.val, (Integer) right.val, op));
				case DOUBLE:
					return new Entry(Entry.Type.DOUBLE, interpretAdd((Double) left.val, (Double) right.val, op));
				case BOOL:
					throw new TypeException();
				case STRING:
					if(op == Token.PLUS)
						return new Entry(Entry.Type.STRING, ((String) left.val) + ((String) right.val));
					else
						throw new InvalidOperationException();
				default:
					throw new InvalidOperationException();
			}
		}else if(left.type == Entry.Type.INT && right.type == Entry.Type.DOUBLE) {
			return new Entry(Entry.Type.DOUBLE, interpretAdd((Integer) left.val, (Double) right.val, op));
		}else if(left.type == Entry.Type.DOUBLE && right.type == Entry.Type.INT) {
			return new Entry(Entry.Type.DOUBLE, interpretAdd((Double) left.val, (Integer) right.val, op));
		}else if(left.type == Entry.Type.STRING && op == Token.PLUS) {
			switch(right.type) {
				case INT:
				case DOUBLE:
				case BOOL:
					return new Entry(Entry.Type.STRING, ((String) left.val) + ((String) right.val));
				default:
					throw new InvalidOperationException();
			}
		}
		
		throw new TypeException();
	}
	
	private int interpretAdd(Integer left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left + right;
			case MINUS:
				return left - right;
			default:
				throw new InvalidOperationException();
		}
	}
	
	private double interpretAdd(Double left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left + right;
			case MINUS:
				return left - right;
			default:
				throw new InvalidOperationException();
		}
	}
	
	private double interpretAdd(Integer left, Double right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left.doubleValue() + right;
			case MINUS:
				return left.doubleValue() - right;
			default:
				throw new InvalidOperationException();
		}
	}
	
	private double interpretAdd(Double left, Integer right, Token op) throws InterpreterRuntimeException {
		switch(op) {
			case PLUS:
				return left + right.doubleValue();
			case MINUS:
				return left - right.doubleValue();
			default:
				throw new InvalidOperationException();
		}
	}
	
	private Entry interpretMultiplyExpression(Tree tree) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left);
		Entry right = interpretExpression(tree.right);
		Token op = tree.token.token;
		if(left.type == right.type) {
			switch(left.type) {
			case INT:
				return new Entry(Entry.Type.INT, interpretMultiply((Integer) left.val, (Integer) right.val, op));
			case DOUBLE:
				return new Entry(Entry.Type.DOUBLE, interpretMultiply((Double) left.val, (Double) right.val, op));
			case BOOL:
				throw new TypeException();
			case STRING:
				throw new TypeException();
			default:
				throw new InvalidOperationException();
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
		
		throw new TypeException();
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
				throw new InvalidOperationException();
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
				throw new InvalidOperationException();
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
				throw new InvalidOperationException();
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
				throw new InvalidOperationException();
		}
	}
	
	private Entry interpretUnaryExpression(Tree tree) throws InterpreterRuntimeException {
		if(tree.left.type == TreeType.LEAF && tree.left.token.token == Token.NOT) {
			Entry right = interpretExpression(tree.right);
			if(right.type == Entry.Type.BOOL) {
				return new Entry(Entry.Type.BOOL, !((Boolean) right.val));
			}
			
			throw new TypeException();
		}
		
		throw new InvalidOperationException();
	}
	
	private Entry interpretPostfixExpression(Tree tree) throws InterpreterRuntimeException {
		switch(tree.type) {
		case CALL:
			return interpretCall(tree);
		case ID:
			return interpretIDExpression(tree);
		case ROLL:
			return interpretRollExpression(tree);
		default:
			return interpretLiteralExpression(tree);
		}
	}
	
	private Entry interpretIDExpression(Tree tree) throws InterpreterRuntimeException {
		return interpretIDExpressionHelper(tree, this.env);
	}
	
	private Entry interpretIDExpressionHelper(Tree tree, Environment env) throws InterpreterRuntimeException {
		if(env != null) {
			String id = interpretLeaf(tree.left);
			Entry res = env.envGet(id);
			if(res == null) 
				throw new ExistenceException();
			
			if(res.type == Entry.Type.ENV)
				return interpretIDExpressionHelper(tree.right, (Environment) res.val);
			
			return res;
		}
		
		throw new ExistenceException();
	}
	
	private Entry interpretLiteralExpression(Tree tree) throws InterpreterRuntimeException {
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
					throw new TypeException();
			}
		}
		
		throw new InvalidOperationException();
	}
	
	// TODO: Implement custom dice rolling.  Probably through a 'standard library' call to roll()?
	private Entry interpretRollExpression(Tree tree) throws InterpreterRuntimeException {
		Entry left = interpretExpression(tree.left);
		Entry right = interpretExpression(tree.right);
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
		
		throw new TypeException();
	}
	
	private String interpretLeaf(Tree tree) throws InterpreterRuntimeException {
		return tree.token.match;
	}
	
	private void interpretEmpty(Tree tree) {}

}
