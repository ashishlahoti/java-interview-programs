package com.example.dp;

public class Test {

	public static void main(String[] args) {
		System.out.println(substring("abcdefgh") + "#");
	}
	
	
	public static String substring(String s) {
		if(s.length() == 1) {
			System.out.println(s);
			return s;
		}
		System.out.println(s);
		return substring(s.substring(0, s.length()/2)) + substring(s.substring(s.length()/2, s.length()));
		
	}
}
