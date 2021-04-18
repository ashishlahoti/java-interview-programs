import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrabTest2 {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(solution(new int[] { 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1 },
				new int[] { 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1 })));
	}

	public static int[] solution(int[] A, int[] B) {
		int num1 = bitsToDecimal(A);
		int num2 = bitsToDecimal(B);
		int sum = num1 + num2;
		return decimalToBits(sum);
	}

	public static int bitsToDecimal(int[] bits) {
		int result = 0;
		for (int i = 0; i < bits.length; i++) {
			int bit = bits[i];
			if (i % 2 != 0) {
				bit *= -1;
			}
			result += bit * Math.pow(2, i);
		}
		return result;
	}

	public static int[] decimalToBits(int num) {
		List<Integer> list = new ArrayList<>();
		while (num != 0) {
			int remainder = num % -2;
			num /= -2;
			if(remainder < 0) {
				remainder += 2;
				num += 1;
			}
			list.add(remainder);
			
		}
		return list.stream().mapToInt(j -> j).toArray();
	}
}
