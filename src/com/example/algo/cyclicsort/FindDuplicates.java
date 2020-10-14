package com.example.algo.cyclicsort;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an array containing n numbers taken from the range 1 to n. It can have
 * duplicates. Find all those missing numbers.
 */
public class FindDuplicates {

	public static void main(String[] args) {
		int[] nums = new int[] { 2, 3, 1, 8, 2, 3, 5, 1 };
		List<Integer> duplicates = findDuplicates(nums);
		System.out.println(duplicates);
	}

	public static List<Integer> findDuplicates(int[] nums) {
		List<Integer> duplicates = new ArrayList<>();
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != i + 1) {
				swap(nums, i, nums[i] - 1);
			}
		}

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
