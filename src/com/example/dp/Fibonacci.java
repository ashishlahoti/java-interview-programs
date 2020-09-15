package com.example.dp;

public class Fibonacci {

	public static void main(String[] args){
		System.out.println(fibonacci(0));
		System.out.println(fibonacci(1));
		System.out.println(fibonacci(2));
		System.out.println(fibonacci(3));
	}
	
	public static int fibonacci(int n){
		if(n == 0 || n == 1){
			return 1;
		}
		return fibonacci(n-1) + fibonacci(n-2);
	}
}
