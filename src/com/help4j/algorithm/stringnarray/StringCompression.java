package com.help4j.algorithm.stringnarray;

public class StringCompression {

	public static void main(String[] args){
		System.out.println(compress("aabbbccddddee"));
		System.out.println(compress("abcde"));
	}
	
	public static String compress(String s){
		StringBuilder builder = new StringBuilder();
		int countConsecutive = 0;
		for(int i=0; i< s.length(); i++){
			if (i < s.length()-1 && s.charAt(i) == s.charAt(i+1)){
				builder.append(s.charAt(i));
				countConsecutive++;
			}else{
				if(countConsecutive > 0){
					builder.append(countConsecutive);
					countConsecutive = 0;
				}
			}
		}
		
		return builder.length() == 0 ? s : builder.toString();
	}
}
