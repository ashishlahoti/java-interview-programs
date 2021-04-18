package com.example.sort;

import java.util.Arrays;

public class QuickSort {

	public static void main(String[] args){
		QuickSort.sort(new int[]{1, 7, 6, 8, 2, 5, 3, 8, 4, 9});
	}
	
	private static void sort(int[] array){
		int lowerIndex = 0;
		int higherIndex = array.length - 1;
		partition(array, lowerIndex, higherIndex);
		System.out.print(Arrays.toString(array));
	}
	
	private static void partition(int[] array, int lowerIndex, int higherIndex){
		int i = lowerIndex;
		int j = higherIndex;
		int pivot = array[(lowerIndex + higherIndex)/2];
		System.out.println("Bef pivot:" + pivot + " " + Arrays.toString(array));
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
		System.out.println("Aft pivot:" + pivot + " " + Arrays.toString(array));
		
		if(lowerIndex < j){
			partition(array, lowerIndex, j);
		}
		if(higherIndex > i){
			partition(array, i, higherIndex);
		}
	}
}