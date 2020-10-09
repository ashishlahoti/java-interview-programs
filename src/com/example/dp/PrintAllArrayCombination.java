package com.example.dp;

public class PrintAllArrayCombination {

	
	public static void main(String[] args) {
		permute(new int[] {1, 2, 3});
	}
	
	public static void permute(int[] nums){
		printAll(nums, 0, nums.length-1);
	}
	
	public static void printAll(int[] nums, int startIndex, int lastIndex){
        if(startIndex == lastIndex){
            print(nums);          
        }else{
            for(int i= startIndex; i<=lastIndex; i++){
                swap(nums, i, startIndex); 
                printAll(nums, startIndex+1, lastIndex);
                swap(nums, i, startIndex);
            }
        }
    }
	
	public static void print(int[] array) {
		for(int i: array) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
	
	public static void swap(int[] array, int i, int j){
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp; 		
	}
}
