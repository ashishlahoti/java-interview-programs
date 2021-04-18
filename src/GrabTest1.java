
public class GrabTest1 {

	public static void main(String[] args) {
		System.out.println(solution(new int[] {1 , 4, -1, 3, 2}));
		System.out.println(solution(new int[] {-1}));
		System.out.println(solution(new int[] {1, 2, 3, 4}));
		System.out.println(solution(new int[] {1, 2, 2, -1}));
	}
	
	public static int solution(int[] A) {
		int i=0;
		int count=1;
		while(A[i] != -1) {
			i= A[i];
			// checks if i(th) index is beyond array or i(th) index stuck in the loop
			if(i > A.length-1 || i == A[i]) {
				return 0;
			}
			count++;
		}
		return count;
	}
}
