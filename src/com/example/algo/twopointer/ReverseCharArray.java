package com.example.algo.twopointer;

import java.util.Arrays;

/**
 * Reverse the string given as char array
 */
public class ReverseCharArray {

	public static void main(String[] args) {
		char[] str = new char[] {'h','e','l','l','o'};
		reverseString(str);
		System.out.println(Arrays.toString(str));
		
	}

	public static void reverseString(char[] s) {
		int i = 0;
		int j = s.length - 1;
		while (i < j) {
			char temp = s[i];
			s[i] = s[j];
			s[j] = temp;
			i++;
			j--;
		}
	}
}
