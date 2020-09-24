package com.example.array;

public class MaxSumContiguousSubarray {
	
	public static void main(String[] args) {
		System.out.println(maxSubArray(new int[] {1, -2, 3, 4, 5}));
		System.out.println(maxSubArray(new int[] {-2, -3, 4, -1, -2, 1, 5, -3}));
	}

	public static int maxSubArray(int[] nums) {
		int max_sum = Integer.MIN_VALUE; 
		int track_sum = 0;
		for(int i=0; i< nums.length; i++) {
			track_sum = track_sum + nums[i];
			if(track_sum > max_sum) {
				max_sum = track_sum;
			}
			if(track_sum < 0) {
				track_sum = 0;
			}
		}
		return max_sum;
	}
}
