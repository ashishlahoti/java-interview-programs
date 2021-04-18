package com.example.algo.OnetoN;

import java.util.Arrays;

/**
 * Given an array containing n distinct numbers in the range [1, n]
 * 1. Sort them using cyclic sort
 * 2. Find first missing positive
 */
public class CyclicSort {

	public static void main(String[] args) {
		int[] nums = new int[] { 4, 3, 2, 5, 1 };
		cyclicSort(nums);
		System.out.println(Arrays.toString(nums));
		nums = new int[] { 4, 1, 5, 3, 2 };
		cyclicSort(nums);
		System.out.println(Arrays.toString(nums));
		nums = new int[] { 4, 1, 6, 3, 2 };
		cyclicSort(nums);
		System.out.println(Arrays.toString(nums));
		System.out.println(findFirstMissingPositive(nums));
	}

	public static void cyclicSort(int[] nums) {
		int n = nums.length;
		int i = 0;
		while (i < n) {
			int index = nums[i] - 1;
			if (index < n && i != index) {
				swap(nums, i, index);
			} else {
				i++;
			}
		}
	}

	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	public static int findFirstMissingPositive(int[] nums) {
		cyclicSort(nums);
		int n = nums.length;
		int i = 0;
		
		while(i < n) {
			if(nums[i] != i+1) {
				return i+1;
			}
			i++;
		}
		return i+1;
	}
}
