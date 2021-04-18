package com.example.algo.backtrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArraySubsets {

	public static void main(String[] args) {
		System.out.println(subsets(new int[] {1, 2, 3}));
		System.out.println(subsets(new int[] {4, 4, 4, 1, 4}));
	}
	public static List<List<Integer>> subsets(int[] nums) {
	    List<List<Integer>> list = new ArrayList<>();
	    Arrays.sort(nums); // Not required when array doesn't contain duplicate elements
	    backtrack(list, new ArrayList<>(), nums, 0);
	    return list;
	}

	private static void backtrack(List<List<Integer>> list , List<Integer> tempList, int [] nums, int index){
	    list.add(new ArrayList<>(tempList));
	    for(int i = index; i < nums.length; i++){
	    	if(i > index && nums[i] == nums[i-1]) continue; // to skip duplicate subsets
	        tempList.add(nums[i]);
	        backtrack(list, tempList, nums, i + 1);
	        tempList.remove(tempList.size() - 1);
	    }
	}
}
