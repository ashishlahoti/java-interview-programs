package com.example.array;

import java.util.Arrays;

public class NextPermutation {

	public static void main(String[] args) {
		int num[] = new int[] { 1, 2, 3, 4, 5 };
		nextPermutation(num);
		System.out.println(Arrays.toString(num));

		num = new int[] { 5, 4, 3, 2, 1 };
		nextPermutation(num);
		System.out.println(Arrays.toString(num));

		num = new int[] { 1 };
		nextPermutation(num);
		System.out.println(Arrays.toString(num));

		num = new int[] { 5, 1, 4, 3, 2 };
		nextPermutation(num);
		System.out.println(Arrays.toString(num));

		num = new int[] { 4, 5, 2, 3, 1 };
		nextPermutation(num);
		System.out.println(Arrays.toString(num));

		num = new int[] { 4, 3, 5, 4, 2, 1 };
		nextPermutation(num);
		System.out.println(Arrays.toString(num));

	}

	public static void nextPermutation(int[] nums) {
		if (nums == null || nums.length <= 1) {
			return;
		}
		int lastIndex = nums.length - 1;
		for (int i = lastIndex; i > 0; i--) {
			if (nums[i] > nums[i - 1]) {
				int nextLowestIndex = i;
				int diff = nums[i] - nums[i - 1];
				for (int j = i + 1; j < nums.length; j++) {
					int difftemp = nums[j] - nums[i - 1];
					if (difftemp > 0 && difftemp < diff) {
						diff = difftemp;
						nextLowestIndex = j;
					}
				}
				swap(nums, nextLowestIndex, i - 1);

				for (int j = i; j < nums.length; j++) {
					for (int k = j + 1; k < nums.length; k++) {
						if (nums[j] > nums[k]) {
							swap(nums, j, k);
						}
					}
				}
				return;
			}
		}

		Arrays.sort(nums);
	}

	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
