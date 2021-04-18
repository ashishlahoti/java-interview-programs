package com.example.algo.dynamicprogramming;

/**
 * You are a professional robber planning to rob houses along a street. Each
 * house has a certain amount of money stashed, the only constraint stopping you
 * from robbing each of them is that adjacent houses have security system
 * connected and it will automatically contact the police if two adjacent houses
 * were broken into on the same night.
 * 
 * Determine the maximum amount of money you can rob tonight without
 * alerting the police?
 */
public class HouseRobber {

	public static void main(String[] args) {
		System.out.println(rob(new int[] {5,3,4,11,2}));
	}
	public static int rob(int[] nums) {
		if(nums.length == 0) return 0;
        if(nums.length == 1) return nums[0];
        
		int[] cache = new int[nums.length];
		cache[0] = nums[0];
		cache[1] = Math.max(nums[0], nums[1]);

		for (int i = 2; i < nums.length; i++) {
			// Math.max(when_not_rob_prev_house, when_rob_prev_house);
			cache[i] = Math.max(cache[i - 2] + nums[i], cache[i - 1]);
		}
		return cache[nums.length - 1];
	}
}
