package com.example.algo.recursive;

/**
 * Distinct ways to keep N balls into K Boxes?
 */
public class DistinctWaysNBallsKBoxes {

	public static void main(String[] args) {
		//N=6, K=2
		distinctWays(6, 2);
	}

	private static void distinctWays(int balls, int boxes) {
		distinctWays(new int[balls], balls, 0, boxes);
	}

	private static void distinctWays(int[] array, int sum, int k, int box) {
		if (sum == 0 && k == box) {
			printArray(array, k);
		} else if (sum > 0) {
			for (int i = 1; i <= sum; i++) {
				array[k] = i;
				distinctWays(array, sum - i, k + 1, box);
			}
		}
	}

	private static void printArray(int[] array, int m) {
		for (int i = 0; i < m; i++) {
			System.out.print(array[i]);
		}
		System.out.println();
	}
	
}
