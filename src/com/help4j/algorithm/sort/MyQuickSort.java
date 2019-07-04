package com.help4j.algorithm.sort;

public class MyQuickSort {
	public static void main(String[] args){
		MyQuickSort.sort(new int[]{1, 7, 6, 8, 5, 2, 3, 4, 9});
	}
	
	private static void sort(int[] array){
		int lowerIndex = 0;
		int higherIndex = array.length - 1;
		partition(array, lowerIndex, higherIndex);
	}
	
	private static void partition(int[] array, int lowerIndex, int higherIndex){
		int i = lowerIndex;
		int j = higherIndex;
		int pivot = array[lowerIndex + ((higherIndex - lowerIndex)/2)];
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
		System.out.println("array : " + printNumbers(array) + ", pivot : "  + pivot + ", l : " + lowerIndex + ", h : " + higherIndex);
		if(lowerIndex < j){
			partition(array, lowerIndex, j);
		}
		if(higherIndex > i){
			partition(array, i, higherIndex);
		}
	}
	
	private static String printNumbers(int[] array) {
		StringBuilder builder = new StringBuilder("[");
		for(int i : array){
			builder.append(i + " ");
		}
		builder.append("]");
		return builder.toString();
    }
}
