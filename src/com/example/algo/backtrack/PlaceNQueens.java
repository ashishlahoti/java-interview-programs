package com.example.algo.backtrack;

import java.util.Arrays;

/**
 * Place 4 queens in 4x4 chess board such that their placement is correct.
 * A queen can not be placed in same row, same column or same diagonal of another queen.
 *
 */
public class PlaceNQueens {

	public static void main(String[] args) {
		placeNQueens(new int[] {0, 1, 2, 3}, 0);
	}
	
	/**
	 * @param nums - index of array represent row and value at each index represent column of chess board.
	 * @param index - start from 0 to N to find all combinations in recursive tree
	 */
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
