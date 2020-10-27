package com.example.string;

public class LongestCommonSubstring {

	public static void main(String[] args) {
		System.out.println(longestCommonSubsequence("abcd", "bcde"));
		System.out.println(longestCommonSubstring("aeeebcd", "ghbcde"));
		System.out.println(lcs("abcd", "bcde", 4, 4, 0));
	}
	
	public static int longestCommonSubsequence(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i < s1.length(); ++i) {
        	for (int j = 0; j < s2.length(); ++j) {
        		if (s1.charAt(i) == s2.charAt(j)) dp[i + 1][j + 1] = 1 + dp[i][j];
                else dp[i + 1][j + 1] =  Math.max(dp[i][j + 1], dp[i + 1][j]);
        	}              
        }
            
        return dp[s1.length()][s2.length()];
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
	
	
	private static int lcs(String s1, String s2, int m, int n, int count) {
	    if (m == 0 || n == 0)
	        return count;
	    if (s1.charAt(m - 1) == s2.charAt(n - 1)) {
	        count = lcs(s1, s2, m - 1, n - 1, count + 1);
	    }
	    count = Math.max(count, Math.max(lcs(s1, s2, m, n - 1, 0), lcs(s1, s2, m - 1, n, 0)));
	    return count;
	}
}
