package com.example.algo.slidingwindow;

/**
 * Find the max sum of contiguous sub array of size k
 */
public class MaxSumOfSubArrayOfSizeK {

	public static void main(String[] args) {
		System.out.println(findMaxSumSubArray(3, new int[] { 2, 1, 5, 1, 3, 2 }));
		System.out.println(findMaxSumSubArray(2, new int[] { 2, 3, 4, 1, 5 }));
		System.out.println(findMaxSumSubArray1(3, new int[] { 2, 1, 5, 1, 3, 2 }));
		System.out.println(findMaxSumSubArray1(2, new int[] { 2, 3, 4, 1, 5 }));
	}

	// Naive Approach O(n*k)
	public static int findMaxSumSubArray(int k, int[] arr) {
		int maxSum = 0, windowSum;
		for (int i = 0; i <= arr.length - k; i++) {
			windowSum = 0;
			for (int j = i; j < i + k; j++) {
				windowSum += arr[j];
			}
			maxSum = Math.max(maxSum, windowSum);
		}

		return maxSum;
	}
	
	// Sliding Window Approach O(n)
	public static int findMaxSumSubArray1(int k, int[] arr) {
	    int windowSum = 0, maxSum = 0;
	    int windowStart = 0;
	    int windowEnd = 0;
	    while (windowEnd < arr.length) {
	      windowSum += arr[windowEnd++]; // add the next element
	      // slide the window, we don't need to slide if we've not hit the required window size of 'k'
	      if (windowEnd >= k) {
	        maxSum = Math.max(maxSum, windowSum);
	        windowSum -= arr[windowStart]; // subtract the element going out
	        windowStart++; // slide the window ahead
	      }
	    }

	    return maxSum;
	  }
}
