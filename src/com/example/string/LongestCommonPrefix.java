package com.example.string;

public class LongestCommonPrefix {

	public static void main(String[] args) {
		System.out.println("output:" + longestCommonPrefix(new String[] { "flower", "flow", "flight" }));
		System.out.println("output:" + longestCommonPrefix(new String[] { "dog", "racecar", "car" }));
		System.out.println("output:" + longestCommonPrefix(new String[] { "ab", "a" }));
		System.out.println("output:" + longestCommonPrefix(new String[] {}));
	}

	//Vertical Scanning Approach - Check char by char across string, starting from first char
	public static String longestCommonPrefix(String[] strs) {
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
