package com.example.algo.dynamicprogramming;

public class LongestCommonSubsequence {

	public static void main(String[] args) {
		System.out.println(longestCommonSubsequenceLength("abcdke", "albcdme"));
		System.out.println(longestCommonSubsequenceLength("JAVAID", "JAVAAID"));
	}

	public static int longestCommonSubsequenceLength(String s1, String s2) {
		int[][] dp = new int[s1.length() + 1][s2.length() + 1];
		for (int i = 0; i <= s1.length(); ++i) {
			for (int j = 0; j <= s2.length(); ++j) {
				if (i == 0 || j == 0) {
					dp[i][j] = 0;
				} else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
				}
			}
		}
		return dp[s1.length()][s2.length()];
	}
}
