package com.example.numbers;

/**
 * Count the number of prime numbers less than a non-negative number, n.
 */
public class CountPrimeNumbers {
	public static void main(String[] args) {
		System.out.println(countPrimes(10));
	}

	public static int countPrimes(int n) {
        int count = 0;
        boolean[] notprime = new boolean[n];
        for(int i=2; i<n; i++){
            if(!notprime[i]){
                count++;
                for(int j=2; i*j<n; j++){
                    notprime[i*j] = true;
                }
            }
            
        }
        return count;
    }
}
