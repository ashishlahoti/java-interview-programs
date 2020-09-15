package com.example.numbers;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RomanToInteger {

	@Test
	public void testRomanToInteger() {
		assertEquals(romanToInteger1("I"), 1);
		assertEquals(romanToInteger1("II"), 2);
		assertEquals(romanToInteger1("III"), 3);
		assertEquals(romanToInteger1("IV"), 4);
		assertEquals(romanToInteger1("V"), 5);
		assertEquals(romanToInteger1("VI"), 6);
		assertEquals(romanToInteger1("VII"), 7);
		assertEquals(romanToInteger1("VIII"), 8);
		assertEquals(romanToInteger1("IX"), 9);
		assertEquals(romanToInteger1("X"), 10);

		assertEquals(romanToInteger1("XL"), 40);
		assertEquals(romanToInteger1("LX"), 60);
		assertEquals(romanToInteger1("XC"), 90);
		assertEquals(romanToInteger1("CX"), 110);
	}

	// Approach 1
	public static int romanToInteger1(String roman) {

		int number = 0;
		for (int i = 0; i < roman.length(); i++) {
			char c = roman.charAt(i);
			switch (c) {
			case 'I':
				number = (i != roman.length() - 1 && (roman.charAt(i + 1) == 'V' || roman.charAt(i + 1) == 'X'))
						? number - 1
						: number + 1;
				break;
			case 'V':
				number += 5;
				break;
			case 'X':
				number = (i != roman.length() - 1 && (roman.charAt(i + 1) == 'L' || roman.charAt(i + 1) == 'C'))
						? number - 10
						: number + 10;
				break;
			case 'L':
				number += 50;
				break;
			case 'C':
				number = (i != roman.length() - 1 && (roman.charAt(i + 1) == 'D' || roman.charAt(i + 1) == 'M'))
						? number - 100
						: number + 100;
				break;
			case 'D':
				number += 500;
				break;
			case 'M':
				number += 1000;
				break;
			}
		}
		return number;

	}

	// Approach 2
	public static int romanToInteger2(String s) {

		Map<Character, Integer> values = new HashMap<>();
		values.put('I', 1);
		values.put('V', 5);
		values.put('X', 10);
		values.put('L', 50);
		values.put('C', 100);
		values.put('D', 500);
		values.put('M', 1000);

		int number = 0;
		for (int i = 0; i < s.length(); i++) {
			if (values.get(s.charAt(i)) >= values.get(s.charAt(i + 1))) {
				number += values.get(s.charAt(i));
			} else {
				number -= values.get(s.charAt(i));
			}
		}
		return number;
	}
}
