package com.help4j.core;

public class PrintAllArrayCombination {

	public static void main(String[] args) {
		print(new int[]{1, 2, 3, 4, 5, 6}, 6, 3, 0, new int[3], 0);
		//printCombination(new int[]{1, 2, 3, 4, 5}, 0, 5, 3);
		
	}
	
	 static void print(int arr[], int n, int r, int index, int data[], int i){
		// Current combination is ready to be printed, print it
		if (index == r) {
			for (int j=0; j<r; j++) {
				System.out.print(data[j]+" ");
			}
			System.out.println("");
			return;
		}
			
		// When no more elements are there to put in data[]
		if (i >= n)
			return;
		
		// current is included, put next at next location
		data[index] = arr[i];
		print(arr, n, r, index+1, data, i+1);
		
		// current is excluded, replace it with next (Note that
		// i+1 is passed, but index is not changed)
		print(arr, n, r, index, data, i+1);
	}
	 
	 static void printCombination(int arr[], int start, int end, int r){
		 if(start > end-r){
			 return;
		 }
		 for(int i=start; i<start+r; i++){
			 System.out.print(arr[i]);
		 }
		 System.out.println();
		 printCombination(arr, start+1, end, r);
	}
}
