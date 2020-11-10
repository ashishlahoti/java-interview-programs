package com.example.algo.dynamicprogramming;

/**
 * Tribonacci Series: 0, 1, 1, 2, 4, 7, 13, 24, 44, ....
 * 
 * Trib(n) = Trib(n-1) + Trib(n-2) + Trib(n-3)
 */
public class Tribonacci {

	public static void main(String[] args) {
		System.out.println(tribonacci(30));
		System.out.println(tribonacci1(30));
	}

	// Approach 1
	public static int tribonacci(int n) {
		if (n == 0 || n == 1) {
			return n;
		}
		int[] memo = new int[n + 1];
		memo[0] = 0;
		memo[1] = 1;
		memo[2] = 1;
		for (int i = 3; i <= n; i++) {
			memo[i] = memo[i - 1] + memo[i - 2] + memo[i - 3];
		}
		return memo[n];
	}

	// Approach 2
	public static int tribonacci1(int n) {
		if (n == 0 || n == 1) {
			return n;
		}
		int first = 0;
		int second = 1;
		int third = 1;
		for (int i = 3; i <= n; i++) {
			int fourth = first + second + third;
			first = second;
			second = third;
			third = fourth;
		}
		return third;
	}
}
