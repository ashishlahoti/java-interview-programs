package com.example.array;

public class FindTwoLargestNumbersInArray {

	public static void main(String[] args) {
		printLargestNumbers(new int[]{1, 2, 3, 4, 5});
	}
	
	public static void printLargestNumbers(int[] array){
		int num1 = Integer.MIN_VALUE;
		int num2 = Integer.MIN_VALUE;
		for (int i = 0; i < array.length; i++) {
			if(array[i] > num1){
				num2 = num1;
				num1 = array[i];
			}
		}
		System.out.println("Larget number: " +num1);
		System.out.println("Second Largest number: " + num2);
	}
}
