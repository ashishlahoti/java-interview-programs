package com.example.array;

import java.util.Arrays;

/**
 * Rotate array clockwise by given number of times
 */
public class RotateArray {

	public static void main(String[] args) {
		int[] nums = new int[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		rotateByK(nums, 2, 0);
		System.out.println(Arrays.toString(nums));
		
		rotateByK(nums, 3, 0);
		System.out.println(Arrays.toString(nums));
		
		rotateByK(nums, 5, 0);
		System.out.println(Arrays.toString(nums));
		
		rotateByK(nums, 6, 0);
		System.out.println(Arrays.toString(nums));
		
		nums = new int[]{ 3, 8, 9, 10, 11, 7, 6 };
		rotateByK(nums, 3, 0);
		System.out.println(Arrays.toString(nums));
		
		nums = new int[]{ 3, 8, 9, 7, 6 };
		rotateByK(nums, 3, 0);
		System.out.println(Arrays.toString(nums));
		
	}

	public static void rotateByOne(int[] nums) {
		int lastIndex = nums.length - 1;
		int temp = nums[lastIndex];
		while (lastIndex > 0) {
			nums[lastIndex] = nums[--lastIndex];
		}
		nums[lastIndex] = temp;
		System.out.println(Arrays.toString(nums));
	}

	public static void rotateByK(int[] nums, int k, int start) {
		k = k % nums.length;
		if (start >= nums.length-1) {
			return;
		}
		
		for (int i = start, j = 0; i < (start + k); i++, j++) {
			if(i < nums.length && nums.length - k + j > start){
				swap(nums, i, nums.length - k + j);
			}			
		}
	
		rotateByK(nums, k, start + k);	
	}
	
	public void rotate(int[] nums, int k) {
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

	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
