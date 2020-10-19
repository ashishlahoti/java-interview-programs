package com.example.algo.recursive;

public class Tribonacci {

	public static void main(String[] args) {
		System.out.println(tribonacci1(30));
		System.out.println(tribonacci2(30));
		System.out.println(tribonacci3(30));
		System.out.println(tribonacci4(30));
	}

	// Approach 1
	public static int tribonacci1(int n) {
		if (n == 0) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		return tribonacci1(n - 1) + tribonacci1(n - 2) + tribonacci1(n - 3);
	}

	// Approach 2
	public static int tribonacci2(int n) {
		int[] memo = new int[n + 1];
		return tribonacci2(n, memo);
	}

	private static int tribonacci2(int n, int[] memo) {
		if (n == 0) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		if (memo[n] > 0) {
			return memo[n];
		}
		memo[n] = tribonacci2(n - 1) + tribonacci2(n - 2) + tribonacci2(n - 3);
		return memo[n];
	}

	// Approach 3
	public static int tribonacci3(int n) {
		if (n == 0 || n == 1) {
			return n;
		}
		int[] memo = new int[n+1];
		memo[0] = 0;
		memo[1] = 1;
		memo[2] = 1;
		for (int i = 3; i <= n; i++) {
			memo[i] = memo[i - 1] + memo[i - 2] + memo[i - 3];
		}
		return memo[n];
	}
	
	// Approach 4
	public static int tribonacci4(int n) {
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
