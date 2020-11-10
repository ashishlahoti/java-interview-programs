package com.example.algo.dynamicprogramming;

import java.util.Arrays;

/**
 * How many ways you can climb to given number of stairs
 * Given that you can take either one step or two steps at a time. 
 * 
 * dp[i] = dp[i-1] + dp[i-2]
 */
public class ClimbingStairs {

	public static void main(String[] args) {
		System.out.println(climbStairs(6));
	}
	
	private static final int climbStairs(int destStep) {
		int[] dp = new int[destStep + 1];
		dp[0] = 1;
		dp[1] = 1;
		for(int i=2; i<=destStep; i++) {
			dp[i] = dp[i-1] + dp[i-2];
		}
		Arrays.toString(dp);
		return dp[destStep];
	}
	
}
