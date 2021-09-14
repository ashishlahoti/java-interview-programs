package com.example.algo.dynamicprogramming;

/**
 * Fibonacci Series: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55 ....
 * 
 * Fib(n) = Fib(n-1) + Fib(n-2)
 *
 */
public class Fibonacci {

	public static void main(String[] args) {
		System.out.println("Fibonacci(0) = " + fibonacci(0));
		System.out.println("Fibonacci(1) = " + fibonacci(1));
		System.out.println("Fibonacci(2) = " + fibonacci(2));
		System.out.println("Fibonacci(3) = " + fibonacci(3));
		System.out.println("Fibonacci(5) = " + fibonacci(5));
		System.out.println("Fibonacci(10) = " + fibonacci(10));
	}

	public static int fibonacci(int n) {
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
