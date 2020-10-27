package com.example.array;

public class FindCommonInThreeSortedArrays {

	public static void main(String[] args) {
		findCommon(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, new int[]{2, 4, 6, 8, 10, 12}, new int[]{3, 6, 9, 12});
		
	}
	
	public static void findCommon(int[] array1, int[] array2, int[] array3){
		
		int i = 0;
		int j = 0;
		int k = 0;
	
		while(i < array1.length && j < array2.length && k < array3.length){
			if(array1[i] == array2[j] && array2[j] == array3[k]){
				System.out.println("Common: " + array1[i]);
				i++;
				j++;
				k++;
			}else if(array1[i] < array2[j]){
				i++;
			}else if(array2[j] < array3[k]){
				j++;
			}else{
				k++;
			}
		}
	}
}
