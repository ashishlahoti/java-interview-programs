package com.example.string;

import java.util.Arrays;

/**
 * Anagram strings are having same count of characters in any order
 */
public class Anagram {

	public static void main(String[] args){
		System.out.println(isAnagram("top", "pot"));
		System.out.println(isAnagram("anagram", "nagaram"));
		System.out.println(isAnagram("rat", "car"));
	}
	
	/**
	 * If Strings are anagram, they will be equal after sorting alphabetically
	 * @param s
	 * @param t
	 * @return
	 */
	public static boolean isAnagram(String s, String t){
		if(s == null || t == null) return false;
		if(s.length() != t.length()) return false;
		
		char[] schars = s.toCharArray();
		char[] tchars = t.toCharArray();
		
		Arrays.sort(schars);
		Arrays.sort(tchars);
		
		return Arrays.equals(schars, tchars);
	}
	
	public boolean isAnagram1(String s, String t) {
	    if (s.length() != t.length()) {
	        return false;
	    }
	    int[] counter = new int[26];
	    for (int i = 0; i < s.length(); i++) {
	        counter[s.charAt(i) - 'a']++;
	        counter[t.charAt(i) - 'a']--;
	    }
	    for (int count : counter) {
	        if (count != 0) {
	            return false;
	        }
	    }
	    return true;
	}
}
