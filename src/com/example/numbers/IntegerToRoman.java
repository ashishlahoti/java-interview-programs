package com.example.numbers;

import java.util.TreeMap;

/**
 * Problem: Given an Integer input, convert it to a Roman numeral. Input is
 * within the range from 1 to 3999.
 * 
 * @author ashishkumarlahoti
 *
 */
public class IntegerToRoman {

	public static void main(String[] args) {
		System.out.println(integerToRoman1(1234));
		System.out.println(integerToRoman2(1234));
		System.out.println(integerToRoman3(1234));
		System.out.println(integerToRoman4(1234));
	}

	// Approach 1
	public static String integerToRoman1(int number) {
		StringBuilder s = new StringBuilder();
		while (number >= 1000) {
			s.append("M");
			number -= 1000;
		}
		while (number >= 900) {
			s.append("CM");
			number -= 900;
		}
		while (number >= 500) {
			s.append("D");
			number -= 500;
		}
		while (number >= 400) {
			s.append("CD");
			number -= 400;
		}
		while (number >= 100) {
			s.append("C");
			number -= 100;
		}
		while (number >= 90) {
			s.append("XC");
			number -= 90;
		}
		while (number >= 50) {
			s.append("L");
			number -= 50;
		}
		while (number >= 40) {
			s.append("XL");
			number -= 40;
		}
		while (number >= 10) {
			s.append("X");
			number -= 10;
		}
		while (number >= 9) {
			s.append("IX");
			number -= 9;
		}
		while (number >= 5) {
			s.append("V");
			number -= 5;
		}
		while (number >= 4) {
			s.append("IV");
			number -= 4;
		}
		while (number >= 1) {
			s.append("I");
			number -= 1;
		}
		return s.toString();
	}

	// Approach 2
	private static int[] values = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
	private static String[] romanLiterals = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

	public static String integerToRoman2(int number) {

		StringBuilder s = new StringBuilder();

		for (int i = 0; i < values.length; i++) {
			while (number >= values[i]) {
				number -= values[i];
				s.append(romanLiterals[i]);
			}
		}
		return s.toString();
	}

	// Approach 3
	public static final String integerToRoman3(int number) {

		int i = getFloorIndex(number);
		if (number == values[i]) {
			return romanLiterals[i];
		}

		return romanLiterals[i] + integerToRoman3(number - values[i]);
	}

	private static int getFloorIndex(int number) {
		for (int i = 0; i < values.length; i++) {
			while (number >= values[i]) {
				return i;
			}
		}
		return -1;
	}

	// Approach 4
	private static final TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
	static {
		treemap.put(1000, "M");
		treemap.put(900, "CM");
		treemap.put(500, "D");
		treemap.put(400, "CD");
		treemap.put(100, "C");
		treemap.put(90, "XC");
		treemap.put(50, "L");
		treemap.put(40, "XL");
		treemap.put(10, "X");
		treemap.put(9, "IX");
		treemap.put(5, "V");
		treemap.put(4, "IV");
		treemap.put(1, "I");

	}

	public static final String integerToRoman4(int number) {
		int l = treemap.floorKey(number);
		if (number == l) {
			return treemap.get(number);
		}
		return treemap.get(l) + integerToRoman4(number - l);
	}

}
