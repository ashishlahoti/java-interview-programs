package com.example.array;

import java.util.Arrays;

/**
 * Move all zeros (0) in the array to the end
 */
public class MoveZeros {

	public static void main(String[] args) {
		int[] nums = { 1, 0, 2, 0, 0, 1 };
		moveZeroes(nums);
		System.out.println(Arrays.toString(nums));

		int[] nums1 = { 0, 0, 1 };
		moveZeroes(nums1);
		System.out.println(Arrays.toString(nums1));

	}

	public static void moveZeroes(int[] nums) {
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != 0) {
				for (int j = i; j > 0; j--) {
					if (nums[j - 1] == 0) {
						int num = nums[j];
						nums[j] = nums[j - 1];
						nums[j - 1] = num;
					}
				}
			}
		}
	}
}
