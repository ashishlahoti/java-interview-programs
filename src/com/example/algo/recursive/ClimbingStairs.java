package com.example.algo.recursive;

/**
 * How many ways you can climb to given number of stairs
 * Given that you can take either one step or two steps at a time. 
 */
public class ClimbingStairs {

	public static void main(String[] args) {
		System.out.println(climbStairs(4));
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
}
