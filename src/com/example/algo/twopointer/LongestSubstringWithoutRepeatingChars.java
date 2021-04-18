package com.example.algo.twopointer;

import java.util.HashMap;
import java.util.Map;

public class LongestSubstringWithoutRepeatingChars {
	
	public static void main(String[] args) {
		System.out.println(lengthOfLongestSubstring("bbbb"));
		System.out.println(lengthOfLongestSubstring(""));
		System.out.println(lengthOfLongestSubstring("a"));
		System.out.println(lengthOfLongestSubstring("abcabcba"));
		System.out.println(lengthOfLongestSubstring("abba"));
		System.out.println(lengthOfLongestSubstring("asjrgapa"));
		System.out.println(lengthOfLongestSubstring("dvdf"));
	}

	public static int lengthOfLongestSubstring(String s) {
        int maxCount = 0;
        Map<Character, Integer> map = new HashMap<>(); 
        int start=0;
        int end=0;
        while(end<s.length()){
            char key = s.charAt(end);
            if(map.containsKey(key)){
                //System.out.println(map + " " + maxCount + " " + start + " " + end);
                int index = map.get(key);
                if(index >= start){
                    maxCount = Math.max(maxCount, end-start);               
                    start = map.get(key)+1;
                }                
            }
            map.put(key, end);
            end++;
        }
        //System.out.println(map + " " + maxCount + " " + start + " " + end);
        return Math.max(maxCount, end-start);
    }
}
