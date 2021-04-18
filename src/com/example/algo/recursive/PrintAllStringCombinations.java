package com.example.algo.recursive;

import java.util.HashSet;
import java.util.Set;

public class PrintAllStringCombinations {

	public static void main(String[] args) {
		printPermutations("", "abcd");
	}

	public static void printPermutations(String prefix, String s) {
		System.out.println(prefix);
		if (s.length() <= 0) {
			return;
		}
		for (int i = 0; i < s.length(); i++) {
			printPermutations(prefix + s.charAt(i), s.substring(0, i) + s.substring(i + 1, s.length()));
		}
	}

	public static long substringCalculator(String s) {
		if (s == null || s.length() == 0)
			return 0;
		System.out.println(s);
		Set<String> set = new HashSet<>();
		substringCalculator(s, set);
		return set.size();
	}

	public static void substringCalculator(String s, Set<String> set) {
		if (!set.add(s)) {
			return;
		}
		if (s.length() == 1) {
			return;
		}
		substringCalculator(s.substring(0, s.length() - 1), set);
		substringCalculator(s.substring(1, s.length()), set);
	}
}
