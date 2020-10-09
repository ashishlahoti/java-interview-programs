package com.example.string;

public class LongestCommonPrefix {

	public static void main(String[] args) {
		System.out.println("output:" + longestCommonPrefix(new String[] { "flower", "flow", "flight" }));
		System.out.println("output:" + longestCommonPrefix(new String[] { "dog", "racecar", "car" }));
		System.out.println("output:" + longestCommonPrefix(new String[] { "ab", "a" }));
		System.out.println("output:" + longestCommonPrefix(new String[] {}));
	}

	// first approach - vertical scanning
	public static String longestCommonPrefix(String[] strs) {
		if (strs.length == 0)
			return "";
		StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < strs[0].length(); i++) {
			char c = strs[0].charAt(i);
			for (int j = 1; j < strs.length; j++) {
				if ((i < strs[j].length() && strs[j].charAt(i) != c) || (i >= strs[j].length())) {
					return prefix.toString();
				}
			}
			prefix.append(c);
		}
		return prefix.toString();
	}

	// improvementrs over first approach
	public static String longestCommonPrefix1(String[] strs) {
		if (strs.length == 0)
			return "";

		int minStrLengh = Integer.MAX_VALUE;
		for (int i = 0; i < strs.length; i++) {
			minStrLengh = Math.min(minStrLengh, strs[i].length());
		}

		StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < minStrLengh; i++) {
			char c = strs[0].charAt(i);
			for (int j = 1; j < strs.length; j++) {
				if (strs[j].charAt(i) != c) {
					return prefix.toString();
				}
			}
			prefix.append(c);
		}
		return prefix.toString();
	}
}
