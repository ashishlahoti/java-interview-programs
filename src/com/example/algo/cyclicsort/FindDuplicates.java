package com.example.algo.cyclicsort;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an array containing n numbers taken from the range 1 to n. It can have
 * duplicates. Find all those missing numbers.
 */
public class FindDuplicates {

	public static void main(String[] args) {
		int[] nums = new int[] { 2, 3, 1, 8, 2, 3, 5, 1, 6, 6 };
		List<Integer> duplicates = findDuplicates(nums);
		System.out.println(duplicates);
		
		int[] nums1 = new int[] {2,5,9,6,9,3,8,9,7,1};
		List<Integer> duplicates1 = findDuplicates(nums1);
		System.out.println(duplicates1);
	}

	public static List<Integer> findDuplicates(int[] nums) {
		List<Integer> duplicates = new ArrayList<>();
		
		// Position the elements according to theirs values for e.g. 1 to 0th and 2 to 1st index
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != i + 1) {
				swap(nums, i, nums[i] - 1);
			}
		}

		// If any elements is not positioned correctly after first pass is duplicate
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != i + 1) {
				duplicates.add(nums[i]);
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
