package com.example.array;

import java.util.Arrays;

public class MinimizeTheMaxDifferenceBtwHeights {

	public static void main(String[] args) {
		System.out.println(difference(new int[] {2, 4, 5, 15}, 2));
		System.out.println(difference(new int[] {1, 2, 3, 4}, 2));
	}
	
	/**
	 * 
	 * @param nums - find minimum different between two elements after adding or subtracting k
	 * @param k - either add or subtract from the element
	 * @return
	 */
	public static int difference(int[] nums, int k) {
		if(nums == null || nums.length < 2) {
			return -1;
		}
		Arrays.sort(nums);
		int small = nums[0] + k;
		int big = nums[nums.length-1] - k;
		
		if(small > big) {
			int temp = small;
			small = big;
			big = temp;
		}

		int diff = big - small;
		
		for(int i=1; i<nums.length-1; i++) {
			int max = nums[i] + k; 
			int min = nums[i] - k;		
			diff = Math.min(diff, max-small);
			diff = Math.min(diff, big-min);
		}
		
		
		return diff;
	}
}
