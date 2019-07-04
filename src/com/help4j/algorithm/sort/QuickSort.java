package com.help4j.algorithm.sort;

public class QuickSort {

	public static void main(String[] args){
		QuickSort.sort(new int[]{1, 7, 6, 8, 2, 5, 3, 8, 4, 9});
	}
	
	private static void sort(int[] array){
		int lowerIndex = 0;
		int higherIndex = array.length - 1;
		partition(array, lowerIndex, higherIndex);
		for(int i : array){
			System.out.print(i + " ");
		}
	}
	
	private static void partition(int[] array, int lowerIndex, int higherIndex){
		int i = lowerIndex;
		int j = higherIndex;
		int pivot = array[(lowerIndex + higherIndex)/2];
		while(i <= j){
			while(array[i] < pivot){
				i++;
			}
			while(array[j] > pivot){
				j--;
			}
			if(i <= j){
				int temp = array[i];
				array[i] = array[j];
				array[j] = temp;
				i++;
				j--;
			}
		}
		if(lowerIndex < j){
			partition(array, lowerIndex, j);
		}
		if(higherIndex > i){
			partition(array, i, higherIndex);
		}
	}
}