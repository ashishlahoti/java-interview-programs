package com.example.algo.OnetoN;

import java.util.*;

/**
 * Find the duplicate [0] and missing [1] number from set of numbers 1 to N
 * @author ashishkumarlahoti
 *
 */
public class FindMismatch {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(findErrorNums(new int[] {1, 2, 2, 4})));
		System.out.println(Arrays.toString(findErrorNums(new int[] {1, 1})));
		System.out.println(Arrays.toString(findErrorNums(new int[] {2, 2})));
		System.out.println(Arrays.toString(findErrorNums(new int[] {3, 2, 3})));
		System.out.println(Arrays.toString(findErrorNums(new int[] {1, 5, 3, 2, 2, 7, 6, 4, 8, 9})));
	}

	public static int[] findErrorNums(int[] nums) {
		int ans[] = new int[2];
		for(int i=0; i< nums.length; i++) {
			int index = Math.abs(nums[i])-1;
			
			if(nums[index] > 0) {
				nums[index] *= -1;
			}else {
				ans[0] = index+1;
			}
		}
		for(int i=0; i< nums.length; i++) {
			if(nums[i] < 0) {
				nums[i] *= -1;
			}else {
				ans[1] = i+1;
			}
		}
		
		return ans;
	}
}
