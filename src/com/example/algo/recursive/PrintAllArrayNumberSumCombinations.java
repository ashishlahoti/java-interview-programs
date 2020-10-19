package com.example.algo.recursive;

public class PrintAllArrayNumberSumCombinations {

	public static void main(String[] args) {
		printCombination(4);
	}

	private static void printCombination(int sum) {
		printCombination(new int[sum], sum, 0);
	}

	private static void printCombination(int[] nums, int sum, int index) {
		if(sum <= 0) {
			if(sum == 0) {
				printArray(nums, index);
			}			
		}else {
			for(int i=1; i<= nums.length; i++) {
				nums[index] = i;
				printCombination(nums, sum-i, index+1);
			}
		}		
	}
	
	private static void printArray(int[] nums, int index) {
		for(int i=0; i<index; i++) {
			System.out.print(nums[i] + " ");
		}
		System.out.println();
	}
}
