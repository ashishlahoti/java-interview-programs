package com.example.array;

import java.util.Arrays;

public class FindKthSmallestElement {

	
	public static void main(String[] args) {
		//int[] nums = new int[] { 3, 5, 4, 2, 1, 6};
		//System.out.println(findKthSmallest(nums, 0, nums.length-1, 3));
		//System.out.println(Arrays.toString(nums));	
		
		int[] nums1 = new int[] { 10, 16, 8, 12, 15, 6, 3, 9, 5};
		System.out.println(findKthSmallest(nums1, 0, nums1.length-1, 3));
		System.out.println(Arrays.toString(nums1));
		
	}
	
	/**
	 * Use quick sort with filter out the iterations of other side of kth element
	 */
	public static int findKthSmallest(int[] nums, int low, int high, int k) {
		if(high > low) {		
			//System.out.println(Arrays.toString(nums) + ", low: " + low + ", high: " + high);
			int i = low;
			int j = high;
			int pivot = nums[(low+high)/2];
			//System.out.println("pivot:" + pivot);
			while(i<j) {
				if(nums[i] < pivot) {
					i++;
				}
				if(nums[j] > pivot) {
					j--;
				}
				int temp = nums[i];
				nums[i] = nums[j];
				nums[j] = temp;		
				i++;
				j--;
			}
			if(nums[k-1] == pivot) {
				return pivot;
			}else {
				if(low < j && low < k) {
					return findKthSmallest(nums, low, j, k);
				}
				if(i < high && i < k) {
					return findKthSmallest(nums, i, high, k);
				}
			}			
		}
		return nums[k-1];		
	}
}
