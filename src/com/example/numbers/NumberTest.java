package com.example.numbers;

public class NumberTest {

	public static void main(String[] args) {
		System.out.println(String.format("%.5f", (double)2/(double)5));
		staircase(5);
		miniMaxSum(new int[] {-17, 3, 5, 7, 9});
		miniMaxSum(new int[] {1, 2, 3, 4, 5});
	}
	
	static void staircase(int n) {
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(i+j+1 >= n){
                    System.out.print('#');
                }else {
                	System.out.print(' ');
                }
            }
            System.out.println();
        }
    }
	
	static void miniMaxSum(int[] arr) {
        int maxValue = arr[0];
        int minValue = arr[0];
        long sum = 0;
        for(int n: arr){
            if(n > maxValue){
                maxValue = n;
            }else if(n < minValue){
                minValue = n;
            }
            sum += n;
        }
        System.out.println((sum-maxValue) + " " + (sum-minValue));
    }
}
