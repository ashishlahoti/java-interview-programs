package com.example.algo.recursive;

public class PrintAllArraySubCombinations {

	public static void main(String[] args) {
		printSubArrayCombination(new int[]{1, 2, 3, 4, 5}, 3);		
	}
	
	public static void printSubArrayCombination(int[] nums, int size){
		printArrayCombinations(nums, 0, size);
	}
	
	public static void printArrayCombinations(int[] nums, int index, int size) {
		if(index >= size) {
			print(nums, size);
		}else {
			for(int i=index; i<nums.length; i++) {
				swap(nums, i, index);
				printArrayCombinations(nums, index+1, size);
				swap(nums, index, i);
			}
		}	
	}
	
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}
	
	public static void print(int[] nums, int size) {
		for(int i=0; i<size; i++) {
			System.out.print(nums[i] + ", ");
		}
		System.out.println();
	}
	
}
