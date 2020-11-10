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
}
