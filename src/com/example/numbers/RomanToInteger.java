package com.example.numbers;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class RomanToInteger {

	@Test
	public void testApproach1() {
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
		assertEquals(romanToInteger2("CM"), 900);
		assertEquals(romanToInteger2("MC"), 1100);
	}

	@Test
	public void testApproach2() {
		assertEquals(romanToInteger2("I"), 1);
		assertEquals(romanToInteger2("II"), 2);
		assertEquals(romanToInteger2("III"), 3);
		assertEquals(romanToInteger2("IV"), 4);
		assertEquals(romanToInteger2("V"), 5);
		assertEquals(romanToInteger2("VI"), 6);
		assertEquals(romanToInteger2("VII"), 7);
		assertEquals(romanToInteger2("VIII"), 8);
		assertEquals(romanToInteger2("IX"), 9);
		assertEquals(romanToInteger2("X"), 10);
		assertEquals(romanToInteger2("XL"), 40);
		assertEquals(romanToInteger2("LX"), 60);
		assertEquals(romanToInteger2("XC"), 90);
		assertEquals(romanToInteger2("CX"), 110);
		assertEquals(romanToInteger2("CM"), 900);
		assertEquals(romanToInteger2("MC"), 1100);
	}
	
	@Test
	public void testApproach3() {
		assertEquals(romanToInteger3("I"), 1);
		assertEquals(romanToInteger3("II"), 2);
		assertEquals(romanToInteger3("III"), 3);
		assertEquals(romanToInteger3("IV"), 4);
		assertEquals(romanToInteger3("V"), 5);
		assertEquals(romanToInteger3("VI"), 6);
		assertEquals(romanToInteger3("VII"), 7);
		assertEquals(romanToInteger3("VIII"), 8);
		assertEquals(romanToInteger3("IX"), 9);
		assertEquals(romanToInteger3("X"), 10);
		assertEquals(romanToInteger3("XL"), 40);
		assertEquals(romanToInteger3("LX"), 60);
		assertEquals(romanToInteger3("XC"), 90);
		assertEquals(romanToInteger3("CX"), 110);
		assertEquals(romanToInteger3("CM"), 900);
		assertEquals(romanToInteger3("MC"), 1100);
	}
	
	// Approach 1
	public static final int romanToInteger1(String roman) {

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
	public static final int romanToInteger2(String s) {

		Map<Character, Integer> values = new LinkedHashMap<>();
		values.put('I', 1);
		values.put('V', 5);
		values.put('X', 10);
		values.put('L', 50);
		values.put('C', 100);
		values.put('D', 500);
		values.put('M', 1000);

		int number = 0;
		for (int i = 0; i < s.length(); i++) {
			if (i+1 == s.length() || values.get(s.charAt(i)) >= values.get(s.charAt(i + 1))) {
				number += values.get(s.charAt(i));
			} else {
				number -= values.get(s.charAt(i));
			}
		}
		return number;
	}
	
	// Approach 3
	private static Map<Character, Integer> map = new LinkedHashMap<>();
	static {
		map.put('I', 1);
		map.put('V', 5);
		map.put('X', 10);
		map.put('L', 50);
		map.put('C', 100);
		map.put('D', 500);
		map.put('M', 1000);
	}
	
	public static final int romanToInteger3(String s) {
		return romanToInteger3(s, 0);
	}
	
	private static final int romanToInteger3(String s, int index) {
		if(index == s.length()) {
			return 0;
		}
		return getNumber(s, index) + romanToInteger3(s.substring(index+1, s.length()));
	}
	
	private static final int getNumber(String s, int index) {
		if(index+1 == s.length()) {
			return map.get(s.charAt(index));
		}
		
		if (map.get(s.charAt(index)) >= map.get(s.charAt(index+1))) {
			return map.get(s.charAt(index));
		} else {
			return -map.get(s.charAt(index));
		}		
	}
	
	
	public static void main(String[] args) {
		System.out.println(romanToInteger3("VI"));
	}
	
}
