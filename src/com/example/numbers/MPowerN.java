package com.example.numbers;

public class MPowerN {

	public static void main(String[] args){
		System.out.println(MPowerN.pow(2, 5));
		System.out.println(MPowerN.pow(2, -3));
	}
	
	private static double pow(double m, long n){
		if(n == 0) return 1;
		if(n < 0) {
			n = -n;
			m = 1/m;
		}
		return (n%2 == 0) ? pow(m*m, n/2) : m*pow(m*m, n/2);
	}
	
}
