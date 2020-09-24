package com.example.array;

public class RemoveDuplicatesFromSortedArray {

	public static void main(String[] args) {
		int[] nums = new int[] { 0, 0, 1, 1, 1, 2, 2, 3, 3, 4 };
		int len = removeDuplicates(nums);
		for (int i = 0; i < len; i++) {
			System.out.println(nums[i]);
		}
	}

	public static int removeDuplicates(int[] nums) {
		if(nums.length == 0) return 0;
		int i = 0;
		for (int j = 1; j < nums.length; j++) {
			if(nums[i] != nums[j]) {
				i++;
				nums[i] = nums[j];
			}
		}
		return i+1;
	}
}
