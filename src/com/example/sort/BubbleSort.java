package com.example.sort;
/**
 *Bubble Sort is nothing but a comparison algorithm where - 
 *- At the end of first iteration, largest element in the array get placed at last index
 *- At the end of second iteration, second largest element in the array get placed at second last index and so on...
 *This way large elements are moving towards the last indexes and hence small elements are moving towards the starting indexes 
 *which is also termed as smaller elements "bubble" to the top of the list that is why it is called bubble sort.
 *
 * 1 -> 10 5 6 8 4
 *      -- - - - -
 * 2 -> 5 6 8 4 10
 *      - - - - 
 * 3 -> 5 6 4 8 10
 *      - - - 
 * 4 -> 5 4 6 8 10
 *      - -
 * 5 -> 4 5 6 8 10
 */
public class BubbleSort {

	public static void main(String[] args){
		int[] array = new int[]{5, 1, 12, -5, 16};
		BubbleSort.sort(array);
	}
	
	private static void sort(int[] array){
		int count = 0;
		for(int i = array.length ; i > 0 ; i --, count++){
			for(int j = 0 ; j < i-1 ; j++, count++){
				if(array[j] > array[j+1]){
					swapNumbers(j, j+1, array);
				}
			}
		}
		printNumbers(array, count);
	}
	
	private static void swapNumbers(int i, int j, int[] array) {
        int temp;
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
	
	private static void printNumbers(int[] array, int count) {
		System.out.print("Sorted Array : {");
		for(int i : array){
			System.out.print(i + " ");
		}
		System.out.print("}, n : " + array.length + ", comparisons : " + count);
    }
}
