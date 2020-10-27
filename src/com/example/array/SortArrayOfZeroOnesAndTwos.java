package com.example.array;

import java.util.Arrays;

public class SortArrayOfZeroOnesAndTwos {

	public static void main(String[] args) {
		sort(new int[] { 0, 1, 2, 0, 1, 2, 0, 1, 2 });
		sort(new int[] { 2, 1, 0 });
		sort(new int[] { 2, 2, 2, 1, 1, 1, 0, 0, 0});
	}

	public static void sort(int[] nums) {
		int i=0;
		int j=nums.length-1;
		
		while(i<j) {
			//System.out.println(Arrays.toString(nums) + ", i:" + i + ", j: " + j);
			while(nums[i] == 0) {
				i++;
			}
			while(nums[j] == 2) {
				j--;
			}
			if(nums[i] == 1) {
				if(nums[j] == 1) {
					int k = j-1;
					while(i<k) {
						if(nums[k] == 2) {
							swap(nums, j, k);
							break;
						}
						k--;
					}
					j--;
				}else if(nums[j] == 0) {
					swap(nums, i, j);
					i++;
				}
			}			
			if(nums[i] == 2) {
				swap(nums, i, j);
				if(nums[i] == 0) {
					i++;
				}
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
