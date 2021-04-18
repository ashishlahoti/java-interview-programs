package com.example.algo.OnetoN;

import java.util.HashSet;
import java.util.Set;

/**
 * Given an array containing n numbers taken from the range 1 to n. It can have
 * duplicates. Find all those missing numbers.
 */
public class FindDuplicates {

	public static void main(String[] args) {
		int[] nums = new int[] { 2, 3, 1, 8, 2, 3, 5, 1, 6, 6 };
		Set<Integer> duplicates = findDuplicates(nums);
		System.out.println(duplicates);

		int[] nums1 = new int[] { 2, 5, 9, 6, 9, 3, 8, 9, 7, 1 };
		Set<Integer> duplicates1 = findDuplicates(nums1);
		System.out.println(duplicates1);
	}

	public static Set<Integer> findDuplicates(int[] nums) {
		Set<Integer> duplicates = new HashSet<>();

		// Mark the number@positionIndex as -ve.
		// Check if number@positionIndex already -ve means duplicate.
		for (int i = 0; i < nums.length; i++) {
			int positionIndex = Math.abs(nums[i]) - 1;
			if (nums[positionIndex] < 0) {
				duplicates.add(Math.abs(nums[i]));
			} else {
				nums[positionIndex] *= -1;
			}
		}
		return duplicates;

	}

	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
