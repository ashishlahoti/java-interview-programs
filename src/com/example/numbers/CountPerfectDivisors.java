package com.example.numbers;

public class CountPerfectDivisors {

	public static void main(String[] args) {
		System.out.println(count(0));
		System.out.println(count(1));
		System.out.println(count(2));
		System.out.println(count(3));
		System.out.println(count(4));
		System.out.println(count(5));
		System.out.println(count(6));
		System.out.println(count(7));
		System.out.println(count(16));
	}
	
	public static int count(int num) {
		int count = 0;
		
		for(int i = 1; i<=num; i++) {
			int square = i*i;
			if(square > num) {
				break;
			}
			if(num%square == 0) {
				count++;
			}
		}
		return count;
	}
}
