package com.example.array;

/**
 * Remove the duplicate elements from the array and move the remaining arrays to the left
 * Return the count of remaining elements
 */
public class RemoveDuplicatesFromSortedArray {

	public static void main(String[] args) {
		int[] nums = new int[] { 0, 0, 1, 1, 1, 2, 2, 3, 3, 4 };
		int len = removeDuplicates(nums);
		for (int i = 0; i < len; i++) {
			System.out.println(nums[i]);
		}
	}
	
	public static int removeDuplicates(int[] nums) {
		int cursor = 0;
		for(int i = 1; i< nums.length; i++) {
			if(nums[i] > nums[cursor]) {
				cursor++;
				nums[cursor] = nums[i];				
			}
		}
		return cursor+1;
	}
}
