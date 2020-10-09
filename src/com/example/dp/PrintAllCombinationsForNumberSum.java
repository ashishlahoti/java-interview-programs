package com.example.dp;

public class PrintAllCombinationsForNumberSum {

	public static void main(String[] args) {
		printCombination(4);
	}

	private static void printCombination(int sum) {
		printCombination(new int[sum], sum, 0);
	}

	private static void printCombination(int[] array, int sum, int index) {
		if (sum <= 0) {
			printArray(array, index);
		} else {
			for (int i = 1; i < array.length; i++) {
				array[index] = i;
				printCombination(array, sum - i, index + 1);
			}
		}

	}

	private static void printArray(int[] array, int n) {
		for (int i = 0; i < n; i++) {
			System.out.print(array[i]);
		}
		System.out.println();
	}
}
