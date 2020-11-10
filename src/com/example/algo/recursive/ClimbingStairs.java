package com.example.algo.recursive;

import java.util.Arrays;

/**
 * How many ways you can climb to given number of stairs
 * Given that you can take either one step or two steps at a time. 
 */
public class ClimbingStairs {

	public static void main(String[] args) {
		System.out.println(climbStairs(6));
		System.out.println(climbStairs1(6));
	}

	public static final int climbStairs(int n) {
		int[] memo = new int[n+1];
		return climbStairs(0, n, memo);
	}

	private static final int climbStairs(int currentStep, int destStep, int[] memo) {

		if(currentStep == destStep) {
			return 1;
		}
		if(currentStep > destStep) {
			return 0;
		}
		if(memo[currentStep] > 0) {
			return memo[currentStep];
		}
		memo[currentStep] = climbStairs(currentStep+1, destStep, memo) + climbStairs(currentStep+2, destStep, memo);
		return memo[currentStep];
	}
	
	
	private static final int climbStairs1(int destStep) {
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
