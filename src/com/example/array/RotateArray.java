package com.example.array;

import java.util.Arrays;

/**
 * Rotate array clockwise by given number of times
 */
public class RotateArray {

	public static void main(String[] args) {
		int[] nums = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		rotateByK(nums, 2, 0);
		System.out.println(Arrays.toString(nums));
	}

	public static void rotateByOne(int[] nums) {
		int lastIndex = nums.length - 1;
		int temp = nums[lastIndex];
		while (lastIndex > 0) {
			nums[lastIndex] = nums[--lastIndex];
		}
		nums[lastIndex] = temp;
		System.out.println(Arrays.toString(nums));
	}

	public static void rotateByK(int[] nums, int k, int start) {
		k = k % nums.length;
		if (start >= nums.length - 1) {
			return;
		}
		for (int i = start, j = 0; i < (start + k); i++, j++) {
			swap(nums, i, nums.length - k + j);
		}
		rotateByK(nums, k, start + k);
	}

	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
