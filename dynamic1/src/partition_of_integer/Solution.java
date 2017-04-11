package partition_of_integer;


public class Solution {
	public static void main(String[] args) {
		Solution s = new Solution(10);
		System.out.println(s.numOfPar());
	}
	public Solution(int size) {
		// TODO Auto-generated constructor stub
		this.size=size;
		waysOfPartition=new long[size+1][size+1];
		for (int i = 0; i <= size; i++) {
			for (int j = 0; j <= size; j++) {
				waysOfPartition[i][j]=-1;
			}
		}
		for (int i = 0; i <= size; i++) {
			waysOfPartition[0][i]=0;
			waysOfPartition[1][i]=1;
			waysOfPartition[i][0]=0;
			waysOfPartition[i][1]=1;
		}
	}
	long numOfPar(){
		for(int i=2;i<=size;i++){
			for(int j=2;j<=size;j++){
				if(i<j) waysOfPartition[i][j]=waysOfPartition[i][i];
				else waysOfPartition[i][j]=calculate(i,j);
			}
		}
		display();
		return waysOfPartition[size][size];	
	}
	long calculate(int i, int j){
		long result;
		if(waysOfPartition[i][j]==-1) {
			result=calculate(i, j-1)+calculate(i-j, j);
		}
		else return waysOfPartition[i][j];
		return result;
	}
	
//	long calculate(int i, int j){
//		long result;
//		if(i<1||j<1) result=0;
//		else if(i==1||j==1) result=1;
//		else result=calculate(i, j-1)+calculate(i-j, j);
//		return result;
//	}
	
	void display() {
		System.out.println("num of partition:");
		System.out.print(String.format("%-4s", ""));
		for (int i = 0; i <= size; i++)
			System.out.print(String.format("%-3s",i));
		System.out.println();
		for (int i = 0; i <= size; i++) {
			System.out.print(String.format("%-4s", i+":"));
			for (int j = 0; j <= size; j++) {
				if (waysOfPartition[i][j]==-1)
					System.out.print(String.format("%-3s", "*"));
				else {
					System.out.print(String.format("%-3s", waysOfPartition[i][j]));
				}
			}
			System.out.println();
		}
	}
	int size;
	long[][] waysOfPartition;
}
