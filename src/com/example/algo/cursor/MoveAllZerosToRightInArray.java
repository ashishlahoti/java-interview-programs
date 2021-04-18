package com.example.algo.cursor;

import java.util.Arrays;

public class MoveAllZerosToRightInArray {

	public static void main(String[] args) {
		int[] nums = new int[] {0,1,0,3,12};
		moveZeroes(nums);
		System.out.println(Arrays.toString(nums));
	}
	public static void moveZeroes(int[] nums) {
        int cursor = 0;
        for(int i=0; i<nums.length; i++){
            if(nums[i] != 0){
                nums[cursor++] = nums[i];
            }
        }
        while(cursor < nums.length){
            nums[cursor++] = 0;
        }
    }
}
