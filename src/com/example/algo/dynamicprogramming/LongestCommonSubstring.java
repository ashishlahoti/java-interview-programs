package com.example.algo.dynamicprogramming;

public class LongestCommonSubstring {

	public static void main(String[] args) {
		System.out.println(longestCommonSubstringLength("abcde", "albcdme"));
		System.out.println(longestCommonSubstring("aeeebcd", "ghbcde"));
	}
	
	public static int longestCommonSubstringLength(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        int length = 0;
        for (int i = 0; i <= s1.length(); ++i) {
        	for (int j = 0; j <= s2.length(); ++j) {
        		if(i==0 || j==0) {
        			dp[i][j] = 0;
        		}else if (s1.charAt(i-1) == s2.charAt(j-1)) {
        			dp[i][j] = dp[i-1][j-1] + 1;
        			length = Math.max(length, dp[i][j]);
        		}else {
        			dp[i][j] = 0;
                }
        	}              
        }
            
        return length;
    }
	
	public static String longestCommonSubstring(String s1, String s2) {
		int[][] dp = new int[s1.length()+1][s2.length()+1];
		int len = Integer.MIN_VALUE;
		int row = 0;
		for (int i = 0; i < s1.length(); ++i) {
        	for (int j = 0; j < s2.length(); ++j) {
        		if (s1.charAt(i) == s2.charAt(j)) {
        			dp[i + 1][j + 1] = 1 + dp[i][j];
        			if(dp[i + 1][j + 1] > len) {
        				len = dp[i + 1][j + 1];
        				row = i;
        			}
        		}
        	}              
        }			
		return s1.substring(row-len+1, row+1);		
	}
}
