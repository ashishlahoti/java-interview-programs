package com.example.string;

/**
 * Palindrom string is equal to its reversed
 * @author aklahoti
 *
 */
public class Palindrom {

	public static void main(String[] args){
		System.out.println(isPalindrom("ababa"));
	}
	
	public static boolean isPalindrom(String s){
		int length = s.length();
		for(int i=0; i<length/2; i++){
			if(s.charAt(i) != s.charAt(length-i-1)){
				return false;
			}
		}
		return true;
	}
}
