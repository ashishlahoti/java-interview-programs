package com.example.algo.backtrack;

import java.util.Arrays;

public class PlaceNQueens {

	public static void main(String[] args) {
		placeNQueens(new int[] {0, 1, 2, 3}, 0);
	}
	
	public static void placeNQueens(int[] nums, int index) {		
		if(index == nums.length) {
			if(isCorrectPlacement(nums)) {
				System.out.println(Arrays.toString(nums));
			}			
		}else {
			for(int i=index; i<nums.length; i++) {
				swap(nums, i, index);		
				placeNQueens(nums, index+1);
				swap(nums, i, index);	
			}	
		}					
	}
	
	public static boolean isCorrectPlacement(int[] nums) {		
		for(int i=1; i< nums.length; i++) {
			if(Math.abs(nums[i]-nums[i-1]) == 1) {
				return false;
			}
		}
		return true;
	}
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
}
