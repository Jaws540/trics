package TIG.scripts.compiler;

import TIG.scripts.Environment;

public abstract class Interpretable {
	
	private Interpreter interp;
	private boolean compiled = false;
	
	public void compile(String rawSrc) {
		if(rawSrc != null) {
			// Set new interpreter
			interp = new Interpreter(rawSrc);
			
			// Compile and flag the result
			compiled = interp.compile();
		}
	}
	
	/**
	 * Used to determine if a script compiled correctly or not
	 * @return true if a correct compile, false if the script failed to compile
	 */
	public boolean isCompiled() {
		return compiled;
	}
	
	/**
	 * Runs this script given an environment
	 * @param env
	 */
	public void run(Environment env) {
		if(compiled)
			interp.run(env);
	}

}
