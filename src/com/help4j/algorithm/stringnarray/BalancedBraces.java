package com.help4j.algorithm.stringnarray;

import java.util.Stack;

/**
 * Asked in Yatra.com online test by hackerrank.
 * Find if baces are balanced or not in given string.
 * @author aklahoti
 *
 */
public class BalancedBraces {
	
	 public static void main(String[] args) {
		String[] test = new String[] {"}][}}(}][))]", "[](){()}", "()", "({}([][]))[]()", "{)[](}]}]}))}(())(", "([[)"};
		
		test = braces(test);
		for(String te : test) {
			System.out.println(te);
		}
	}

	public static String[] braces(String[] values) {
        if(values == null) {
            return new String[]{};
        }
        
        if(values.length < 1) {
            return new String[]{};
        }
        
                
        String[] result = new String[values.length];
        for(int i = 0; i < values.length; i++) {
        	result[i] = checkBraces(values[i]) ? "YES" : "NO";
        }
        return result;
    }
    
    public static boolean checkBraces(String values) {
    	Stack<Character> option = new Stack<>();
    	for(int i = 0; i < values.length(); i++) {
    		char val = values.charAt(i);
    		char stackVal;
    		switch (val) {
			case '{':
			case '[':
			case '(':
				option.push(val);
				break;
			case '}':
				if(option.isEmpty()) {
					return false;
				}
				stackVal = option.pop();
				if(stackVal != '{') {
					return false;
				}
				break;
			case ']':
				if(option.isEmpty()) {
					return false;
				}
				stackVal = option.pop();
				if(stackVal != '[') {
					return false;
				}
				break;
			case ')':
				if(option.isEmpty()) {
					return false;
				}
				stackVal = option.pop();
				if(stackVal != '(') {
					return false;
				}
				break;
			default:
				return false;
			}
    	}
        return option.isEmpty();
    }
}
