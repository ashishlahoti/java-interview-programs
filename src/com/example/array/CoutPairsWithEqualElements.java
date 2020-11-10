package com.example.array;

import java.util.HashMap;
import java.util.Map;

public class CoutPairsWithEqualElements {

	public static void main(String[] args) {
		System.out.println(test1(null));
		System.out.println(test1(new int[] {}));
		System.out.println(test1(new int[] { 1 }));
		System.out.println(test1(new int[] { 1, 1, 2 }));
		System.out.println(test1(new int[] { 1, 1, 1 }));
		System.out.println(test1(new int[] { 1, 2, 3 }));
		System.out.println(test1(new int[] { 1, 1, 1, 1, 1, 1 }));
		System.out.println(test1(new int[100000]));
	}

	public static int test1(int[] nums) {
		// initial check
		if (nums == null || nums.length <= 1) {
			return 0;
		}

		// initialization
		int count = 0;
		Map<Integer, Integer> freqCountMap = new HashMap<>();

		// count frequency of each number
		for (int i = 0; i < nums.length; i++) {
			freqCountMap.put(nums[i], freqCountMap.getOrDefault(nums[i], 0) + 1);
		}

		// count permutation per frequency(n) nC2 = n*(n-1)/2
		for (int freq : freqCountMap.values()) {
			if (freq > 1) {
				count += (freq * (freq - 1)) / 2;
			}
		}

		return count;
	}
}
