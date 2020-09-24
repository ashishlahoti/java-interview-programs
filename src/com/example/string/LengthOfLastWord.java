package com.example.string;

public class LengthOfLastWord {

	public static void main(String[] args) {
		System.out.println(lengthOfLastWord(""));
		System.out.println(lengthOfLastWord("a "));
		System.out.println(lengthOfLastWord(" "));
		System.out.println(lengthOfLastWord("Hello World"));
	}
	
	public static int lengthOfLastWord(String s) {
		int length = 0;	
        for(int i=s.length()-1; i>=0; i--){
        	if(s.charAt(i) == ' ') {
        		if(length > 0) {
        			break;
        		}     		
        	}else {
        		length++;
        	}
        	
        }
		return length;
    }
}
