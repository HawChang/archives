package windy_number;


public class Solution {
	public static void main(String[] args) {
		Solution s = new Solution(500,1000);
		System.out.println(s.numOfWindyNumber());
	}
	public Solution(int start,int end) {
		// TODO Auto-generated constructor stub
		this.start=start;
		this.end=end;
		initialAssistant();
	}
	void initialAssistant(){
		int temp=end;
		digit=1;
		while(temp>=10){
			temp/=10;
			digit++;
		}
		assist=new long[digit+1][10];
		for (int i = 1; i <= digit; i++) {
			for (int j = 0; j < 10; j++) {
				assist[i][j]=0;
			}
		}
		for (int i = 0; i < 10; i++) {
			assist[1][i]=1;
		}
		for (int i = 2; i <= digit; i++) {
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 10; k++) {
					if(Math.abs(j-k)>1) assist[i][j]+=assist[i-1][k];
				}
			}
		}
		display();
	}
	void display() {
		System.out.println("num of partition:");
		System.out.print(String.format("%-4s", ""));
		for (int i = 0; i < 10; i++)
			System.out.print(String.format("%-8s",i));
		System.out.println();
		for (int i = 0; i <= digit; i++) {
			System.out.print(String.format("%-4s", i+":"));
			for (int j = 0; j < 10; j++) {
				//if (waysOfPartition[i][j]==-1)
				//	System.out.print(String.format("%-3s", "*"));
				//else {
					System.out.print(String.format("%-8s", assist[i][j]));
				//}
			}
			System.out.println();
		}
	}
	long numOfWindyNumber(){
		return windyUnder(end+1)-windyUnder(start);
	}
	long windyUnder(int upperBound){
		System.out.println("upperBound="+upperBound);
		long number=0;
		int position=1,temp=upperBound,prev=-1;
		while(temp>=10){
			temp/=10;
			position++;
		}
		for (int i = 1; i < position; i++) {
			for (int j = 1; j < 10; j++) {
				System.out.println("available: assist["+i+"]["+j+"]="+assist[i][j]);
				number+=assist[i][j];
			}
		}
		while(position>0){
			System.out.println("position="+position);
			int head=upperBound%(int)Math.pow(10, position)/(int)Math.pow(10, position-1);
			System.out.println("head="+head+"   prev="+prev);
			for(int i=0;i<head;i++){
				if(Math.abs(i-prev)>1){
					System.out.println("available: assist["+position+"]["+i+"]="+assist[position][i]);
					number+=assist[position][i];
				}
			}
			prev=head;
			position--;
		}
		return number;
	}
	private int start,end,digit;
	long[][] assist;
}
