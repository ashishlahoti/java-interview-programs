package com.help4j.algorithm.sort;
/**
 * At the end of first iteration, first two elements should be sorted in increasing order.
 * At the end of second iteration, first three elements should be sorted in increasing order.
 * 1 -> 10 5 6 8 4
 *      -- -
 * 2 -> 5 10 6 8 4
 *      - -- - 
 * 3 -> 5 6 10 8 4
 *      - - -- -
 * 4 -> 5 6 8 10 4
 *      - - - -- -
 * 5 -> 4 5 6 8 10
 * @author aklahoti
 *
 */
public class InsertionSort {
	
	public static void main(String[] args){
		int[] array = new int[]{5, 2, 4, 6, 1, 3};
		int[] array1 = new int[]{10,34,2,56,7,67,88,42};
		InsertionSort.sort(array);
		InsertionSort.sort(array1);
	}
	
	private static void sort(int[] array){
		for(int i = 1 ; i < array.length ; i++){
			for(int j = i ; j > 0 ; j--){
				if(array[j] < array[j - 1]){
					swapNumbers(j, j - 1, array);
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
