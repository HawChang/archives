package ladder_climbing;


public class Solution {
	public static void main(String[] args) {
		Solution s = new Solution(20);
		System.out.println(s.numOfWays());
	}
	public Solution(int size) {
		// TODO Auto-generated constructor stub
		this.size=size;
		waysOfClimbing=new long[size+1];
		waysOfClimbing[1]=1;
		waysOfClimbing[2]=2;
	}
	long numOfWays(){
		for(int i=3;i<=size;i++){
			waysOfClimbing[i]=waysOfClimbing[i-1]+waysOfClimbing[i-2];
		}
		displayNum();
		return waysOfClimbing[size];
	}
	void displayNum(){
		System.out.println("ways of climbing:");
		for (int i = 1; i <= size; i++) {
			System.out.println(i+":"+waysOfClimbing[i]);
		}
	}
	int size;
	long[] waysOfClimbing;
}
