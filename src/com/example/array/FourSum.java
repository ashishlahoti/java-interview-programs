package com.example.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FourSum {

	public static void main(String[] args) {

		List<List<Integer>> listOfList = fourSum(new int[] { 1,0,-1,0,-2,2 }, 0);
		System.out.println(listOfList);
		listOfList = fourSum(new int[] {}, 0);
		System.out.println(listOfList);		
		listOfList = fourSum(new int[] {0, 0, 0, 0}, 1);
		System.out.println(listOfList);
	}

	public static List<List<Integer>> fourSum(int[] nums, int target) {
		Set<List<Integer>> set = new HashSet<>();
		Arrays.sort(nums);
		for (int i = 0; i < nums.length-3; i++) {
			for(int m = i+1; m < nums.length-2; m++) {
				int j = m + 1;
				int k = nums.length - 1;
				while (j < k) {
					int sum = nums[i] + nums[m] + nums[j] + nums[k];
					if (sum == target) {
						set.add(Arrays.asList(nums[i], nums[m], nums[j++], nums[k--]));
					} else if (sum > target) {
						k--;
					} else {
						j++;
					}
				}
			}
		}
		return new ArrayList<>(set);
	}
}
