package lab6;

public class Optimize {
	public static void main(String args[]){
		TokenAnalysis tokenAnalysis=new TokenAnalysis();
		Token root=tokenAnalysis.run();
		root.show();
		root.optimize();
		if (root.root!=null) {
			root.root.show();
		}else{
			root.show();
		}
	}
}
