package com.example.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ThreeSum {

	public static void main(String[] args) {

		List<List<Integer>> listOfList = threeSum1(new int[] { -1, 0, 1, 2, -1, -4 });
		System.out.println(listOfList);
		listOfList = threeSum1(new int[] { -1, 0, 1, 2, -1, -4 });
		System.out.println(listOfList);
		listOfList = threeSum1(new int[] { 3, 0, -2, -1, 1, 2 });
		System.out.println(listOfList);
	}

	public static List<List<Integer>> threeSum(int[] nums) {
		Set<List<Integer>> listOfList = new HashSet<>();
		Arrays.sort(nums);
		for (int i = 0; i < nums.length; i++) {
			for (int j = i + 1; j < nums.length; j++) {
				for (int k = j + 1; k < nums.length; k++) {
					if (nums[i] + nums[j] + nums[k] == 0) {
						List<Integer> list = new LinkedList<>();
						list.add(nums[i]);
						list.add(nums[j]);
						list.add(nums[k]);
						listOfList.add(list);
					}
				}
			}
		}
		return new LinkedList<>(listOfList);
	}

	public static List<List<Integer>> threeSum1(int[] nums) {
		Set<List<Integer>> set = new HashSet<>();
		Arrays.sort(nums);
		for (int i = 0; i < nums.length - 2; i++) {
			int j = i + 1;
			int k = nums.length - 1;
			while (j < k) {
				int sum = nums[i] + nums[j] + nums[k];
				if (sum == 0) {
					set.add(Arrays.asList(nums[i], nums[j++], nums[k--]));
				} else if (sum > 0) {
					k--;
				} else {
					j++;
				}
			}
		}
		return new ArrayList<>(set);
	}
}
