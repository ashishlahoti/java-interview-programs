package com.example.array;

/**
 * Remove the given element from the array and move the remaining arrays to the
 * left Return the count of remaining elements
 */
public class RemoveElements {

	public static void main(String[] args) {
		int[] nums = new int[] { 1, 3, 2, 2, 3 };
		int length = removeElement(nums, 3);
		for (int i = 0; i < length; i++) {
			System.out.println(nums[i]);
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
