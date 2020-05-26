package com.example.exception;

public class OutOfMemoryError {

	public static void main(String[] args){
			OutOfMemoryError.go(1);
	}
	
	public static void go(int i){
		int[] array = new int[i];
		int size = i;
		while(i > 0){
			array[i-1] = 0;
			i--;
		}
		go(size*5);
	}
	
	public static void generateOOM() throws Exception {
		System.out.println("\n=================> OOM test started..\n");
		for (int i = 1; i >=1; i = i*5) {
			System.out.println("Iteration " + i + " Free Mem: " + Runtime.getRuntime().freeMemory());
			int[] array = new int[i];
			int j = i-1;
			
			// feel memoryFillIntVar array in loop..
			do {
				array[j] = 0;
				j--;
			} while (j > 0);
			System.out.println("\nRequired Memory for next loop: " + i);
			Thread.sleep(1000);
		}
	}
	
}
