package com.help4j.algorithm.sort;
/*
 * At the end of first iteration, smallest element will be placed at first index
 */
public class SelectionSort {

	public static void main(String[] args){
		int[] array = new int[]{5, 1, 12, -5, 16};
		int[] array1 = new int[]{10,34,2,56,7,67,88,42};
		SelectionSort.sort(array);
		SelectionSort.sort(array1);
	}
	
	private static void sort(int[] array){
		for(int i = 0 ; i < array.length ; i++){
			for(int j = i + 1 ; j < array.length ; j++){
				if(array[j] < array[i]){
					swapNumbers(j, i, array);
				}
			}
		}
		printNumbers(array);
	}
	
	private static void swapNumbers(int i, int j, int[] array) {
        int temp;
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
	
	private static void printNumbers(int[] array) {
		for(int i : array){
			System.out.print(i + " ");
		}
		System.out.println();
    }
}
