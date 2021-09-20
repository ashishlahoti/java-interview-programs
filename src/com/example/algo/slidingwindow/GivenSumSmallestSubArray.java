package com.example.algo.slidingwindow;

/**
 * find the length of smallest contiguous sub array whose sum >= given sum
 */
public class GivenSumSmallestSubArray {

	public static void main(String[] args) {
		int result = findMinSubArray(7, new int[] { 2, 1, 5, 2, 3, 2 });
		System.out.println("Smallest subarray length: " + result);
		result = findMinSubArray(7, new int[] { 2, 1, 5, 2, 8 });
		System.out.println("Smallest subarray length: " + result);
		result = findMinSubArray(8, new int[] { 3, 4, 1, 1, 6 });
		System.out.println("Smallest subarray length: " + result);
	}
	
	public static int findMinSubArray(int s, int[] arr) {
		int windowSum = 0, minLength = Integer.MAX_VALUE;
		int windowStart = 0;
		int windowEnd = 0;
		while(windowEnd < arr.length) {
			windowSum += arr[windowEnd++];
			//slide the window if sum >= given sum to check other possible subarrays
			while(windowSum >= s) {
				minLength = Math.min(minLength, windowEnd-windowStart);
				windowSum -= arr[windowStart]; // subtract the element silding away
				windowStart++; // slide window
			}
		}
		return minLength == Integer.MAX_VALUE ? 0 : minLength;
	}

}
