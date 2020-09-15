package com.example.string;

/**
 * Given a string s, find the longest palindromic substring in s.
 * 
 * @author ashishkumarlahoti
 */
public class LongestPalindromicSubstring {
	public static void main(String[] args) {
		System.out.println(longestPalindrome("a"));
		System.out.println(longestPalindrome("babad"));
		System.out.println(longestPalindrome("abcddcbaa"));
	}

	public static String longestPalindrome(String s) {
		String longestPalindrome = "";

		for (int i = 0; i < s.length(); i++) {
			for (int j = i + 1; j <= s.length(); j++) {
				String substring = s.substring(i, j);
				if (substring.length() > longestPalindrome.length() && isPalindrom(substring)) {
					longestPalindrome = substring;
				}
			}
		}

		return longestPalindrome;
	}

	public static boolean isPalindrom(String s) {
		int length = s.length();
		for (int i = 0; i < length / 2; i++) {
			if (!(s.charAt(i) == s.charAt(length - 1 - i))) {
				return false;
			}
		}
		return true;
	}
}
