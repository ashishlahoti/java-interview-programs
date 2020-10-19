package com.example.algo.recursive;

import java.util.Arrays;

public class PrintAllArrayCombinations {

	public static void main(String[] args) {
		printPermutations(new int[] {1, 2, 3, 4}, 0);
	}
	
	/**
	 * @param arr
	 * @param index
	 */
	public static void printPermutations(int[] arr, int index) {
		
		if(index >=arr.length) {	
			System.out.println(Arrays.toString(arr));
			return;
		}
		for(int i=index; i<arr.length; i++) {
			swap(arr, i, index);			
			printPermutations(arr, index+1);
			swap(arr, index, i);	//reverse the previous swap effect	
		}
	}
	
	private static void swap(int[] arr, int i, int j){
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
