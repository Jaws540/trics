package TIG.scripts.compiler;

import java.util.List;

import TIG.scripts.Entry;
import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.CompileException;
import TIG.scripts.compiler.exceptions.ExceptionList;
import TIG.scripts.compiler.exceptions.InterpreterRuntimeException;
import TIG.scripts.compiler.exceptions.InvalidOperationException;
import TIG.scripts.compiler.exceptions.TypeException;
import TIG.scripts.compiler.parse_tree.Tree;
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
				System.out.println(ast);
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
			case DECLARE:
			case IF:
			case WHILE:
			case CALL:
			case ASSIGN:
			case EMPTY:
				interpretEmpty(tree);
			default:
				break;
		}
	}

	private void interpretSequence(Tree tree) throws InterpreterRuntimeException {
		interpret(tree.left);
		interpret(tree.right);
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
			case MUL_EXPR:
			case UNARY_EXPR:
			default:
				throw new InvalidOperationException();
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
		if(left.type == right.type) {
			// TODO: Implement relational expressions
		}
		
		throw new TypeException();
	}
	
	private void interpretEmpty(Tree tree) {
		
	}

}
