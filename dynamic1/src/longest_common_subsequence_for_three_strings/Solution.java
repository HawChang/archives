package longest_common_subsequence_for_three_strings;


public class Solution {
	public static void main(String[] args) {
		String test1,test2,test3;
		test1 = "abcbaa";
		test2 = "abaacaba";
		test3="babbacbabaa";
		Solution s = new Solution(test1,test2,test3);
		s.longestCommonSubsequence();
	}
	public Solution(String tar1,String tar2,String tar3) {
		// TODO Auto-generated constructor stub
		this.tar1 = tar1;
		this.tar2=tar2;
		this.tar3=tar3;
		matchPathNum=new int[tar1.length()+1][tar2.length()+1][tar3.length()+1];
		matchPathPointer=new int[tar1.length()+1][tar2.length()+1][tar3.length()+1];
		
	}
	void longestCommonSubsequence(){
		if(tar1.length()==0||tar2.length()==0||tar3.length()==0){
			System.out.println("no common subsequence");
			return;
		}
		for (int i = 1; i <= tar1.length(); i++) {
			for (int j = 1; j <= tar2.length(); j++) {
				for (int k = 1; k <= tar3.length(); k++) {
					matchPathPointer[i][j][k]=-1;
				}
			}
		}
		for (int i = 0; i <= tar1.length(); i++) {
			for (int j = 0; j <= tar2.length(); j++) {
				matchPathNum[i][j][0]=0;
			}
		}
		for (int i = 0; i <= tar1.length(); i++) {
			for (int j = 0; j <= tar3.length(); j++) {
				matchPathNum[i][0][j]=0;
			}
		}
		for (int i = 0; i <= tar2.length(); i++) {
			for (int j = 0; j <= tar3.length(); j++) {
				matchPathNum[0][i][j]=0;
			}
		}
		for (int i = 1; i <= tar1.length(); i++) {
			for (int j = 1; j <= tar2.length(); j++) {
				for (int k = 1; k <= tar3.length(); k++) {
					if(tar1.charAt(i-1)==tar2.charAt(j-1)&&tar2.charAt(j-1)==tar3.charAt(k-1)) {
						//System.out.println("found match at ["+(i-1)+","+(j-1)+","+(k-1)+"]");
						matchPathPointer[i][j][k]=0;
						matchPathNum[i][j][k]=matchPathNum[i-1][j-1][k-1]+1;
					}
					else{
						int a=matchPathNum[i-1][j][k];
						int b=matchPathNum[i][j-1][k];
						int c=matchPathNum[i][j][k-1];
						if(a>=b&&a>=c) {
							matchPathNum[i][j][k]=a;
							matchPathPointer[i][j][k]=1;
						}
						else if(b>=c) {
							matchPathNum[i][j][k]=b;
							matchPathPointer[i][j][k]=2;
						}
						else {
							matchPathNum[i][j][k]=c;
							matchPathPointer[i][j][k]=3;
						}
					}
				}
			}
		}
		String subSequence=tracePath();
		System.out.print("longest common subsequence:");
		System.out.println(subSequence);
	}
	String tracePath(){
		String result="";
		String path="";
		int posi=tar1.length();
		int posj=tar2.length();
		int posk=tar3.length();
		while(posi>0&&posj>0&&posk>0){
			//System.out.println("i="+posi+"   j="+posj+"   k="+posk);
			switch (matchPathPointer[posi][posj][posk]) {
			case 0:
				//System.out.println("in");
				path="["+posi+","+posj+","+posk+"]="+tar1.charAt(posi-1)+"\n"+path;
				result=tar1.charAt(posi-1)+result;
				posi-=1;
				posj-=1;
				posk-=1;
				break;
			case 1:
				posi-=1;
				break;
			case 2:
				posj-=1;
				break;
			case 3:
				posk-=1;
				break;
			default:
				break;
			}
		}
		System.out.println(path);
		return result;
	}
	private String tar1,tar2,tar3;
	private int[][][] matchPathNum;
	private int[][][] matchPathPointer;
}
