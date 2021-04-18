package com.example.algo.cursor;

/**
 * Given an array nums and a value val, remove all instances of that value
 * in-place and return the new length.
 */
public class RemoveGivenElementFromArray {

	public static void main(String[] args) {
		int[] nums = new int[] { 3, 2, 2, 3 };
		int len = removeElement(nums, 3);
		for (int i = 0; i < len; i++) {
			System.out.print(nums[i] + " ");
		}
		System.out.println();
		nums = new int[] { 0, 1, 2, 2, 3, 0, 4, 2 };
		len = removeElement(nums, 2);
		for (int i = 0; i < len; i++) {
			System.out.print(nums[i] + " ");
		}
	}

	public static int removeElement(int[] nums, int val) {
		int cursor = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != val) {
				nums[cursor] = nums[i];
				cursor++;
			}
		}
		return cursor;
	}
}
