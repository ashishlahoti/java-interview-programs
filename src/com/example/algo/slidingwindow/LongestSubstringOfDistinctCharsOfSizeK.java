package com.example.algo.slidingwindow;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstringOfDistinctCharsOfSizeK {

	public static void main(String[] args) {
		System.out.println("Length of the longest substring: " + findLength("araaci", 2));
		System.out.println("Length of the longest substring: " + findLength("araaci", 1));
		System.out.println("Length of the longest substring: " + findLength("cbbebi", 3));
	}

	public static int findLength(String str, int k) {
		if (str == null || str.length() == 0 || str.length() < k)
			throw new IllegalArgumentException();

		int windowStart = 0;
		int windowEnd = 0;
		int maxLength = Integer.MIN_VALUE;

		Map<Character, Integer> map = new HashMap<>();

		while (windowEnd < str.length()) {
			char rightChar = str.charAt(windowEnd++);
			map.put(rightChar, map.getOrDefault(rightChar, 0) + 1);

			if (map.size() > k) {
				char leftChar = str.charAt(windowStart++);
				map.put(leftChar, map.get(leftChar) - 1);
				if (map.get(leftChar) == 0) {
					map.remove(leftChar);
				}
			}
			maxLength = Math.max(maxLength, windowEnd - windowStart);
		}
		return maxLength == Integer.MIN_VALUE ? 0 : maxLength;
	}

}
