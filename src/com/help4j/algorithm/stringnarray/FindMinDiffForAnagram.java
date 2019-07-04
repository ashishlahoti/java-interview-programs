package com.help4j.algorithm.stringnarray;

import java.util.HashMap;
import java.util.Map;
/**
 * Asked in Yatra.com online test by hackerrank.
 * You have given two string, you have to find out min number of characters remove from each string to make them anagaram strings.
 * @author aklahoti
 */
public class FindMinDiffForAnagram {

	public static void main(String[] args) {
    	String[] a = new String[] {"abce"};
    	String[] b = new String[] {"abfe"};
		
    	int[] val = getMinimumDifference(a, b);
    	
    	for(int te : val) {
			System.out.println(te);
		}
	}
	
	 /*
     * Complete the function below.
     */
    static int[] getMinimumDifference(String[] a, String[] b) {
    	if(a == null || b == null || a.length != b.length) {
    		return new int[0];
    	}
    	
    	int[] result = new int[a.length];
    	for(int i = 0; i < a.length ; i++) {
    		result[i] = getMinimumDifference(a[i].toLowerCase(), b[i].toLowerCase());
    	}
    	return result;
    }
    
    static int getMinimumDifference(String a, String b) {
    	Map<Character, Integer> charCount = new HashMap<>();

    	if(a.length() != b.length()) {
    		return -1;
    	} else if(a.equalsIgnoreCase(b)) {
    		return 0;
    	}
    	
    	for(int i = 0 ; i < a.length() ; i++) {
    		char ch = a.charAt(i);
    		if(!charCount.containsKey(ch)) {
    			charCount.put(ch, 1);
    		} else {
    			charCount.put(ch, charCount.get(ch) + 1);
    		}
    	}
    	
    	int count = 0;
    	for(int i = 0 ; i < b.length() ; i++) {
    		char ch = b.charAt(i);
    		if(!charCount.containsKey(ch)) {
    			count ++;
    		} else {
    			int val = charCount.get(ch) - 1;
    			if(val < 0) {
    				count ++;
    			}else if(val == 0){
    				charCount.remove(ch);
    			} else {
        			charCount.put(ch, val);
    			}
    		}
    	}
    	
    	int nextCount = 0;
    	if(!charCount.isEmpty()) {
    		for(Character chValue : charCount.keySet()) {
    			int value = charCount.get(chValue);
    			nextCount += value;
     		}
    	}
    	System.out.println("Extra Char in String a: " + nextCount);
    	System.out.println("Extra Char in String b: " + count);
    	return count > nextCount ? nextCount : count;
    }
}
