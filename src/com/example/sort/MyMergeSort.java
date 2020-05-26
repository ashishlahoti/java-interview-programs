package com.example.sort;

public class MyMergeSort {
	private static int[] merge = null;

	public static void main(String[] args){
		int[] array = new int[]{38, 27, 43, 3, 9, 82, 10};
		merge = new int[array.length];
		MyMergeSort.sort(array, 0, array.length-1);
	}
	
	private static void sort(int[] array, int lowerIndex, int higherIndex){
		if(lowerIndex < higherIndex){
			int middleIndex = (lowerIndex + higherIndex)/2;
			sort(array, lowerIndex, middleIndex);
			sort(array, middleIndex + 1, higherIndex);
			
			merge(array, lowerIndex, middleIndex, higherIndex);
			
		}
	}
	
	private static void merge(int[] array, int lowerIndex, int middle, int higherIndex){
		for(int i = lowerIndex; i <= higherIndex; i ++){
			merge[i] = array[i];
		}
		int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (merge[i] <= merge[j]) {
                array[k] = merge[i];
                i++;
            } else {
                array[k] = merge[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = merge[i];
            k++;
            i++;
        }
		printNumbers(merge);
		printNumbers(array);
	}
	
	private static void printNumbers(int[] array) {
		for(int i : array){
			System.out.print(i + " ");
		}
		System.out.println();
    }
}
