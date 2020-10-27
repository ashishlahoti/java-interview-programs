package com.example.array;

import java.util.Arrays;

public class Reverse {

	public static void main(String[] args) {
		int[] nums = new int[] {1, 2, 3, 4, 5};
		reverse(nums);
		System.out.println(Arrays.toString(nums));
	}
		
	public static void reverse(int[] nums) {
		if(nums == null || nums.length <= 1) {
			return;
		}
		
		for(int i=0; i<nums.length/2; i++) {
			swap(nums, i, nums.length-i-1);
		}
	}
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
