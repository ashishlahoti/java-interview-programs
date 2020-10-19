package com.example.algo.recursive;

import java.util.LinkedList;
import java.util.List;

public class GenerateParanthesis {

	public static void main(String[] args) {

		List<String> list = generateParenthesis(3);

		for (String s : list) {
			System.out.println(s);
		}
	}

	public static List<String> generateParenthesis(int n) {
		List<String> list = new LinkedList<>();
		generateParenthesis(n, "", list, 0, 0);
		return list;
	}

	public static void generateParenthesis(int n, String s, List<String> list, int open, int close) {
		if (s.length() == n * 2) {
			//if (isValidParantesis(s)) {
				list.add(s);
			//}
			return;
		}
		if(open < n) {
			generateParenthesis(n, s + "(", list, open+1, close);
		}
		if(close < open) {
			generateParenthesis(n, s + ")", list, open, close+1);
		}
	}

	public static boolean isValidParantesis(String s) {
		int balance = 0;
		for (char c : s.toCharArray()) {
			if (c == '(') {
				balance++;
			} else {
				balance--;
			}
			if (balance < 0) {
				return false;
			}
		}
		return balance == 0;
	}
}
