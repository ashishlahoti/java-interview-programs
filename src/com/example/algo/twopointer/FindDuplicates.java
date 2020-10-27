package com.example.algo.twopointer;

/**
 * Find duplicate in an array having n+1 integers within range [1, n]
 * There is only one duplicate in the array.
 */
public class FindDuplicates {

	public static void main(String[] args) {
		System.out.println(findDuplicate(new int[] {2, 5, 9, 6, 9, 3, 8, 9, 7, 1}));
		System.out.println(findDuplicate(new int[] {1, 2, 3, 4, 1}));
	}
	
	public static int findDuplicate(int[] nums) {
		
		int tortoise = nums[nums[0]];
		int hare = nums[tortoise];
		
		//find the point where they meet 
		while(tortoise != hare) {
			hare = nums[nums[hare]];
			tortoise = nums[tortoise];
		}

		//find the point where cycle start
		tortoise = nums[0];
		while(tortoise != hare) {
			hare = nums[hare];
			tortoise = nums[tortoise];
		}
		
		return tortoise;

	}
}
