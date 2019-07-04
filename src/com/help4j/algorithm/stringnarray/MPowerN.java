package com.help4j.algorithm.stringnarray;

public class MPowerN {

	private static int count = 0;
	
	public static void main(String[] args){
		System.out.println(MPowerN.pow(2, 40));
		System.out.println(count);
		count = 0;
		
		System.out.println(MPowerN.power(2, 40));
		System.out.println(count);
	}
	
	private static long power(long m, long n){
		long cutpoint = getCutpoint(n);
		long p = pow(m, n/cutpoint);
		return pow(p, cutpoint);
	}
	
	private static long pow(long m, long n){
		count++;
		return (n > 1) ? m*pow(m, n-1) : m;
	}
	
	private static long getCutpoint(long n){
		long cutpoint = 1;
		for(int i=1; i<n; i++){
			if(n%i != 0) continue;
			long quotientDeviation = n/i - i;
			if(quotientDeviation < 0){
				return i;
			}
		}
		return cutpoint;
	}
}
