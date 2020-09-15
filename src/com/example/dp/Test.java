package com.example.dp;

import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		System.out.println(f(5));
		System.out.println(fibonacci(5));
	}
	
	static Map<Integer, Integer> memory = new HashMap<>();
	
	public static int f(int n){
		if(n <= 2) return 1;
		return f(n-1) + f(n-2); 
	}
	
	public static int fibonacci(int n) {
		if(memory.containsKey(n)) return memory.get(n);
		int number = 0;
		if(n <= 2) {
			number = 1;
		}else {
			number = f(n-1) + f(n-2);
		}
		memory.put(n, number);
		return number;
	}
}
