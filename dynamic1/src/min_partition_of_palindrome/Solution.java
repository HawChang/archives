package min_partition_of_palindrome;

public class Solution {
	public static void main(String[] args) {
		String test;
		//test = "abbabbbb";
		test = "babbabab";
		Solution s = new Solution(test);
		System.out.println(s.minPartition());
	}

	public Solution(String tar) {
		// TODO Auto-generated constructor stub
		this.tar = tar;
		size = tar.length();
		isPalindrome = new boolean[size][size];
		minPar = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				isPalindrome[i][j] = false;
				minPar[i][j] = 0;
			}
		}
		findPalindrome();
	}

	int minPartition() {
		System.out.println("target: "+tar);
		displayIsPalindrome();
		for (int i = 0; i < size; i++) {
			minPar[i][i] = 0;
		}
		for (int i = 0; i < size - 1; i++) {
			minPar[i][i + 1] = isPalindrome[i][i + 1] ? 0 : 1;
		}
		for (int j = 2; j < size; j++) {
			for (int i = 0; i + j < size; i++) {
				if(isPalindrome[i][i+j]==true) minPar[i][i+j]=0;
				else {
					int max=Integer.MAX_VALUE;
					boolean success=false;
					int tempk=-1;
					//System.out.println("searching for "+i+" to "+(i+j));
					for (int k = i; k < i+j; k++) {
						//System.out.println("k="+k+":");
						if(minPar[i][k]+minPar[k+1][i+j]<max){
							success=true;
							max=minPar[i][k]+minPar[k+1][i+j]+1;
							tempk=k;
						}
					}
					if(success) {
						//System.out.println("choose k="+tempk);
						minPar[i][i+j]=max;
					}
					else System.err.println("??????");
				}
			}
		}
		displayMinPar();
		return minPar[0][size-1];
	}

	void findPalindrome() {
		for (int i = 0; i < size; i++) {
			isPalindrome[i][i] = true;
		}
		for (int i = 0; i < size - 1; i++) {
			isPalindrome[i][i + 1] = tar.charAt(i) == tar.charAt(i + 1) ? true : false;
		}
		for (int i = size - 3; i >= 0; i--) {
			for (int j = i + 2; j < size; j++) {
				isPalindrome[i][j] = isPalindrome[i + 1][j - 1] == true && tar.charAt(i) == tar.charAt(j) ? true : false;
			}
		}
	}

	void displayIsPalindrome() {
		System.out.println("isPalindrome:");
		System.out.print("  ");
		for (int i = 0; i < size; i++)
			System.out.print(i + " ");
		System.out.println();
		for (int i = 0; i < size; i++) {
			System.out.print(i + ":");
			for (int j = 0; j < size; j++) {
				if (i > j)
					System.out.print("* ");
				else {
					if (isPalindrome[i][j])
						System.out.print("T ");
					else
						System.out.print("F ");
				}
			}
			System.out.println();
		}
	}
	void displayMinPar() {
		System.out.println("minPar:");
		System.out.print("  ");
		for (int i = 0; i < size; i++)
			System.out.print(i + " ");
		System.out.println();
		for (int i = 0; i < size; i++) {
			System.out.print(i + ":");
			for (int j = 0; j < size; j++) {
				if (i > j)
					System.out.print("* ");
				else {
					System.out.print(minPar[i][j]+" ");
				}
			}
			System.out.println();
		}
	}
	private String tar;
	private int size;
	private boolean[][] isPalindrome;
	private int[][] minPar;
}
