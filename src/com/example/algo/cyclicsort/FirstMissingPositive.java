package com.example.algo.cyclicsort;

/**
 * Find smallest positive integer which does not occur in the sequence
 */
public class FirstMissingPositive {

	public static void main(String[] args) {
		System.out.println(firstMissingPositive(new int[] { 1, 3, 6, 4, 1, 2 }));
		System.out.println(firstMissingPositive(new int[] { 1 }));		
		System.out.println(firstMissingPositive(new int[] { 2, 1 }));
	}

	// Cyclic Sort
	public static int firstMissingPositive(int[] nums) {
		
		if(nums.length == 1) {
			return 1;
		}
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != i + 1) {
				swap(nums, i, nums[i] - 1);
			}
		}
		int i;
		for (i = 0; i < nums.length; i++) {
			if (nums[i] != i + 1) {
				return i+1;
			}
		}	
		return i+1;
	}

	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
