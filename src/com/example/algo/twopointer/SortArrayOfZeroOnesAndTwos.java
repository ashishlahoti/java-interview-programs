package com.example.algo.twopointer;

import java.util.Arrays;

public class SortArrayOfZeroOnesAndTwos {

	public static void main(String[] args) {
		onepasssort(new int[] { 0, 1, 2, 0, 1, 2, 0, 1, 2 });
		onepasssort(new int[] { 2, 1, 0 });
		onepasssort(new int[] { 2, 2, 2, 1, 1, 1, 0, 0, 0});
		onepasssort(new int[] { 2, 1, 0, 0, 0, 1, 2, 2, 1});
	}
	
	public static void onepasssort(int[] nums) {
		int i=0;
		int j=nums.length-1;
		int startCursor = i;
		int endCursor = j;
		while(i < nums.length && j >=0) {
			if(nums[i] == 0) {
				swap(nums, i, startCursor);
				startCursor++;
				i++;
			}else {
				i++;
			}
			
			if(nums[j] == 2) {
				swap(nums, j, endCursor);
				endCursor--;
				j--;
			}else {
				j--;
			}
		}
		System.out.println(Arrays.toString(nums));
	}
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
