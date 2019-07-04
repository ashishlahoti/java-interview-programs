package com.help4j.algorithm.recursive;

public class MPowerN {

	public static void main(String[] args){
		System.out.println(pow(2,3));
	}
	
	public static int pow(int m, int n){
		return (n>1) ? m*pow(m, n-1) : m;
	}
}
