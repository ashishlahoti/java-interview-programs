package com.example.array;

import java.util.HashSet;
import java.util.Set;

/**
 * Find the two numbers in given array whose sum is equal to given number Assume
 * there is only one possible solution
 */
public class TwoSum {

	public static void main(String[] args) {
		int[] sum = twoSum1(new int[] { 1, 2, 3, 4, 5 }, 9);
		System.out.println(sum[0] + "-" + sum[1]);
	}

	public static int[] twoSum(int[] nums, int target) {
		int[] sum = new int[2];
		for (int i = 0; i < nums.length; i++) {
			for (int j = i + 1; j < nums.length; j++) {
				if (nums[i] + nums[j] == target) {
					sum[0] = i;
					sum[1] = j;
					return sum;
				}
			}
		}
		return sum;
	}

	public static int[] twoSum1(int[] nums, int target) {
		int[] sum = new int[2];
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < nums.length; i++) {
			if (set.contains(target-nums[i])) {
				sum[0] = target-nums[i];
				sum[1] = nums[i];
				return sum;
			} else {
				set.add(nums[i]);
			}
		}
		return sum;
	}
}
