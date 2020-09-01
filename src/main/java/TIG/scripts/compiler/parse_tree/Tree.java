package TIG.scripts.compiler.parse_tree;

import TIG.scripts.compiler.MToken;

public class Tree {
	
	public TreeType type;
	public Tree left;
	public Tree right;
	public MToken token;
	
	public Tree(TreeType type) {
		this.type = type;
		this.left = null;
		this.right = null;
		this.token = null;
	}
	
	public Tree(TreeType type, MToken tok) {
		this.type = type;
		this.left = null;
		this.right = null;
		this.token = tok;
	}
	
	public Tree(TreeType type, Tree l, Tree r) {
		this.type = type;
		this.left = l;
		this.right = r;
		this.token = null;
	}
	
	public Tree(TreeType type, Tree l, Tree r, MToken tok) {
		this.type = type;
		this.left = l;
		this.right = r;
		this.token = tok;
	}
	
	@Override
	public String toString() {
		return "(" + 
				type + 
				(token != null ? "[" + token.match + "]" : "") + 
				(left != null && right != null ? ": " + left + ", " + right : "") + 
				")";
	}
	
}
