package lab6;


public class Restriction {
	private String left;
	private String op;
	private String right;
	public String getOp() {
		return op;
	}
	public String getRight() {
		return right;
	}
	
	public Restriction(){
		
	}
	public Restriction(String left, String op, String right){
		this.left=left;
		this.op=op;
		this.right=right;
	}
	public String toString(){
		return left+op+right;
	}
	public String getLeft(){
		return left;
	}
}
