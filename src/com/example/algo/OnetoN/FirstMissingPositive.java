package com.example.algo.OnetoN;

/**
 * Find smallest positive integer which does not occur in the sequence
 */
public class FirstMissingPositive {

	public static void main(String[] args) {
		System.out.println(firstMissingPositive(new int[] { 1, 3, 6, 4, 1, 2 }));
		System.out.println(firstMissingPositive(new int[] { 1 }));
		System.out.println(firstMissingPositive(new int[] { 2, 1 }));
		System.out.println(firstMissingPositive(new int[] { 1, 2, 3 }));
		System.out.println(firstMissingPositive(new int[] { -1, -2, -3 }));
		System.out.println(firstMissingPositive(new int[] { 4, 1, 5, 6, 2 }));
	}

	public static int firstMissingPositive(int[] nums) {
		int size = nums.length - moveNegativesToEnd(nums);
		return firstMissingPositive(nums, size);
	}

	public static int moveNegativesToEnd(int[] nums) {
		int size = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] < 0) {
				int temp = nums[i];
				nums[i] = nums[nums.length - 1 - size];
				nums[nums.length - 1 - size] = temp;
				size++;
			}
		}
		return size;
	}

	public static int firstMissingPositive(int[] nums, int size) {
		for (int i = 0; i < size; i++) {
			int index = Math.abs(nums[i]) - 1;
			if (index < size && nums[index] > 0) {
				nums[index] *= -1;
			}
		}
		int i = 0;
		for (; i < size; i++) {
			if (nums[i] > 0) {
				return i + 1;
			}
		}
		return i + 1;
	}
}
