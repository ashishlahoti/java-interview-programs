package com.example.recursive;

public class PrintAllCombinationOfString {

	public static void main(String[] args){
		String s = "abc";
		for(int i=0; i < s.length(); i++){
			substring(s, "", i);
		}
	}
	
	public static void substring(String content, String part, int index){
		if(index >= content.length()){
			return;
		}
		String sub = part + content.charAt(index);
		
		System.out.println(sub);
		
		substring(content, sub, index + 1); //a index=0 //ab index=1 //abc index=2
	}
	
	public static String swap(String a){
		char[] charArray = new char[a.length()];
		for(int i=0, j=a.length()-1; i <a.length() && j>=0; i++,j--){
			charArray[i] = a.charAt(j);
		}
		return new String(charArray);
	}
}
