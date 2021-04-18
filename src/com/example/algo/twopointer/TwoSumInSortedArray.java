package com.example.algo.twopointer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Find two elements in sorted array whose sum equals to target
 */
public class TwoSumInSortedArray {

	public static void main(String[] args) {
		System.out.println(twoSum(new int[] { -3, 2, 3, 3, 6, 8, 15 }, 6));
	}

	public static List<Integer> twoSum(int[] numbers, int target) {
		Arrays.sort(numbers);
		int start = 0;
		int end = numbers.length - 1;
		while (start < end) {
			int total = numbers[start] + numbers[end];
			if (total == target) {
				return new ArrayList<>(Arrays.asList(numbers[start], numbers[end]));
			} else if (total > target) {
				end--;
			} else {
				start++;
			}
		}
		return new ArrayList<>();
	}
}
