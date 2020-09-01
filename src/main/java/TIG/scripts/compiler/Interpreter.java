package TIG.scripts.compiler;

import java.util.List;

import TIG.scripts.Environment;
import TIG.scripts.compiler.exceptions.CompileException;
import TIG.scripts.compiler.exceptions.ExceptionList;
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
	
	public int interpret() {
		return 0;
	}

}
