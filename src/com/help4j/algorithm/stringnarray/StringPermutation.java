package com.help4j.algorithm.stringnarray;

public class StringPermutation {
	public static void main(String[] args){
		String s = "abc";
		for(int i=0; i< s.length(); i++){
			patterns("abc", "", i);
		}
		System.out.println("================");
		permutation("abcd", "");
		
	}
	
	public static void patterns(String str, String prefix, int index) {
		if(index >= str.length()){
			return;
		}
		String content = prefix + str.charAt(index);
		System.out.println(content);
		patterns(str, content, index + 1);
	}
	
	public static void permutation(String str, String prefix){
		for(int i=0; i<str.length(); i++){
			String part1 = str.substring(0, i);
			String part2 = str.substring(i+1);
			System.out.println(part1 + "," + part2);
		}
	}
	
	
	/*public static void permutation(String str, String prefix) {
		if (str.length() == 0) {
			System.out.println(prefix);
		} else {
			for (int i= 0; i < str.length(); i++) {
				String rem = str.substring(0, i) + str.substring(i + 1);
				permutation(rem, prefix + str.charAt(i));
			}
		}
	}*/
}
