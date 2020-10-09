package com.example.dp;

public class PrintStringCombinations {

	public static void main(String[] args) {
		printCombinations("abcd");
	}

	private static final void printCombinations(String s) {
		for (int i = 0; i < s.length(); i++) {
			printCombinations("", s, i);
		}
	}

	private static final void printCombinations(String prefix, String s, int index) {
		if (index >= s.length()) {
			return;
		}

		String content = prefix + s.charAt(index);
		// System.out.println(content);
		permutation("", content);
		printCombinations(content, s, index + 1);

		// printCombinations(prefix, s.charAt(index) + s.substring(0, index) + s.substring(index+1, s.length()), index+1);
		// printCombinations(s.charAt(index)+"", s.substring(0, index) + s.substring(index+1, s.length()), 0);
	}

	private static final void permutation(String prefix, String s) {
		if (s.length() == 0) {
			System.out.println(prefix);
		}
		for (int i = 0; i < s.length(); i++) {
			permutation(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, s.length()));
		}
	}
}
