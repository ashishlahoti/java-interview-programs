package com.example.numbers;

public class HappyNumber {

	public static void main(String[] args) {
		System.out.println(isHappy(19));
		System.out.println(isHappy(4));
		System.out.println(isHappy(1));
		System.out.println(isHappy(0));
	}

	public static boolean isHappy(int n) {

		int tortoise = squareSumOfDigits(n);
		int hair = squareSumOfDigits(tortoise);

		while (hair != tortoise) {
			tortoise = squareSumOfDigits(tortoise);
			hair = squareSumOfDigits(squareSumOfDigits(hair));
		}
		
		if (tortoise == 1) {
			return true;
		}

		return false;
	}

	public static int squareSumOfDigits(int n) {
		int sum = 0;
		while (n > 0) {
			int num = n % 10;
			n = n / 10;
			sum = sum + (num * num);
		}
		return sum;
	}
}
