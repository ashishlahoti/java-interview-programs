package com.example.algo.cyclicsort;

import java.util.Arrays;

/**
 * Given an array of size n, containing 1 to n unique numbers in any sequence, sort them
 */
public class CyclicSort {

	public static void main(String[] args) {
		int[] nums = new int[] {5, 3, 2, 4, 1};
		sort(nums);
		System.out.println(Arrays.toString(nums));
	}

	public static void sort(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != i+1) {
				swap(nums, i, nums[i]-1);
			}
		}
	}

	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
