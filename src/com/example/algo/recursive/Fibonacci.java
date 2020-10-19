package com.example.algo.recursive;

public class Fibonacci {

	public static void main(String[] args) {
		fibonacci(10);
	}

	/**
	 * print fibonacci series from 0 to n
	 * @param n
	 */
	private static void fibonacci(int n) {
		for (int i = 0; i <= n; i++) {
			System.out.print(fib(i) + ", ");
		}
	}

	/**
	 * Recursive approach f(n) = f(n-1) + f(n-2)
	 * @param n
	 * @return
	 */
	public static int fib(int n) {
		if (n <= 1) {
			return n;
		}
		return fib(n - 1) + fib(n - 2);
	}

	// Dynamic Programming
	public static int fibonacci1(int n) {
		if (n <= 1) {
			return n;
		}
		int[] fib = new int[n + 1];
		fib[0] = 0;
		fib[1] = 1;
		for (int i = 2; i <= n; i++) {
			fib[i] = fib[i - 1] + fib[i - 2];
		}
		return fib[n];
	}
}
