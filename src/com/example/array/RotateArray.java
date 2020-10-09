package com.example.array;

/**
 * Rotate array clockwise by given number of times
 */
public class RotateArray {

	public static void main(String[] args) {
		int[] nums = { 1, 2, 3, 4, 5 };
		rotate(nums, 6);
		for (int i : nums) {
			System.out.println(i);
		}
	}

	public static void rotate(int[] nums, int k) {
		k = k % nums.length;
		int[] newArray = new int[nums.length];

		for (int i = 0; i < nums.length; i++) {
			if (i < nums.length - k) {
				newArray[i + k] = nums[i];
			} else {
				newArray[i + k - nums.length] = nums[i];
			}
		}
		for (int i = 0; i < nums.length; i++) {
			nums[i] = newArray[i];
		}
	}
}
