package com.example.algo.silidingwindow;

import java.util.Arrays;

/**
 * Find the average of all contiguous sub arrays of size k
 */
public class AverageOfSubArrayOfSizeK {
	
	public static void main(String[] args) {
		double[] result = findAverages(5, new int[] { 1, 3, 2, 6, -1, 4, 1, 8, 2 });
		System.out.println("Averages of subarrays of size K: " + Arrays.toString(result));
		
		result = findAverages1(5, new int[] { 1, 3, 2, 6, -1, 4, 1, 8, 2 });
		System.out.println("Averages of subarrays of size K: " + Arrays.toString(result));
	}
	
	// Naive Approach O(n*k)
	public static double[] findAverages(int K, int[] arr) {
		double[] result = new double[arr.length - K + 1];
		for (int i = 0; i <= arr.length - K; i++) {
			// find sum of next 'K' elements
			double sum = 0;
			for (int j = i; j < i + K; j++)
				sum += arr[j];
			result[i] = sum / K; // calculate average
		}

		return result;
	}
	
	
	// Sliding Window Approach O(n)
	public static double[] findAverages1(int k, int[] arr) {
		double[] result = new double[arr.length - k + 1];
		int windowStart=0;
		int windowEnd=0;
		double windowSum=0;
		while(windowEnd < arr.length) {
			windowSum += arr[windowEnd++];
	
			if(windowEnd >= k) {
				System.out.println(windowSum);
				result[windowStart] = windowSum/k;
				windowSum -= arr[windowStart];
				windowStart++;
			}
		}
		return result;
	}	
}
