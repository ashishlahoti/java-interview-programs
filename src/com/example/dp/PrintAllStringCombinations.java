package com.example.dp;

public class PrintAllStringCombinations {

	public static void main(String[] args) {
		printPermutations("", "abcd");
	}
	
	public static void printPermutations(String prefix, String s) {		
		System.out.println(prefix);
		if(s.length() <= 0) {
			return;
		}		
		for(int i=0; i<s.length(); i++) {
			printPermutations(prefix+s.charAt(i), s.substring(0, i) + s.substring(i+1, s.length()));
		}
	}
}
