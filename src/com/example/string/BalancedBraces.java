package com.example.string;

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
    	Stack<Character> stack = new Stack<>();
    	for(int i = 0; i < values.length(); i++) {
    		char val = values.charAt(i);
    		switch (val) {
			case '{':
			case '[':
			case '(':
				stack.push(val);
				break;
			case '}':
				if(stack.isEmpty() || stack.pop() != '{') {
					return false;
				}
				break;
			case ']':
				if(stack.isEmpty() || stack.pop() != '[') {
					return false;
				}
				break;
			case ')':
				if(stack.isEmpty() || stack.pop() != '(') {
					return false;
				}
				break;
			default:
				return false;
			}
    	}
        return stack.isEmpty();
    }
}
