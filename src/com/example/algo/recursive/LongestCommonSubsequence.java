package com.example.algo.recursive;

public class LongestCommonSubsequence {

	public static void main(String[] args) {
		System.out.println(longestCommonSubsequence("abcdefghijk", "abcdxfghijl", 0, 0));
	}
	private static int longestCommonSubsequence(String s1, String s2, int i, int j) {
		if(i ==s1.length() || j ==s2.length()) {
			return 0;
		}
		//If both chars are equal
		if(s1.charAt(i) == s2.charAt(j)) {
			return 1 + longestCommonSubsequence(s1, s2, i+1, j+1);
		}
		//If both chars are not equal
		return Math.max(longestCommonSubsequence(s1, s2, i+1, j), longestCommonSubsequence(s1, s2, i, j+1));
	}
}
