package com.example.string;

public class ReverseWords {

	public static void main(String args[]){
		String s = "my name is ashish";
		char[] s1 = s.toCharArray();
		reverse(s1, 0, s1.length);
		int start = 0;
		for(int i=0; i< s1.length; i++){
			if(s1[i] == ' ' || i==s1.length){
				reverse(s1, start, i);
				start = i+1;
			}
		}
		System.out.println(new String(s1));
	}
	
	public static void reverse(char[] s, int start, int end){
		int length = start+end;
		for(int i=start; i < length/2; i++){
			char temp = s[i];
			s[i] = s[length-i-1];
			s[length-i-1] = temp;
		}
	}
}
