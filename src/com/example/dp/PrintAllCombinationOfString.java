package com.example.dp;

public class PrintAllCombinationOfString {

	public static void main(String[] args) {
		String s = "abcd";
		for (int i = 0; i < s.length(); i++) {
			substring(s, "", i);
		}
		
		permutation("", "abc");
	}
	
	public static void substring(String content, String part, int index) {
		if (index >= content.length()) {
			return;
		}
		String sub = part + content.charAt(index);

		System.out.println(sub);
		//permutation("", sub);

		substring(content, sub, index + 1);
	}

	private static void permutation(String prefix, String str) {
		int n = str.length();
		if (n == 0) {
			System.out.println(prefix);
		} else {
			for (int i = 0; i < n; i++) {
				permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
			}
		}
	}
}
