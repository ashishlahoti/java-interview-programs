package com.example.algo.recursive;

public class WayToKeepNBallsIntoKBoxes {

	public static void main(String[] args) {
		combination(4, 2);
	}
	
	private static void combination(int N, int K){
		int[] numbers = new int[N+1];
		for(int i=0; i<N; i++) {
			numbers[i] = i+1; 
			System.out.println(i+1);
		}
		
		System.out.println(numbers);
		int counter = 0;
		for(int i=0; i<N; i++) {
			for(int j=i; j<N; j++) {
				int sum = numbers[i];
				
				System.out.print(numbers[i] + " ");
				for(int k=0; k<K-1; k++) {
					if(j+k >= N) {
						break;
					}
					sum = sum + numbers[j+k];
					System.out.print(numbers[j+k] + " ");
				}
				
				if(sum == N) {
					counter++;
				}
				System.out.println("--");
			}
		}
		System.out.println(counter);
	}
}
