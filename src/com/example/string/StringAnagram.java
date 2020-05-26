package com.example.string;
/**
 * Anagram strings can be formed by rearranging the letters of another
 * @author aklahoti
 *
 */
public class StringAnagram {

	public static void main(String[] args){
		System.out.println(isAnagram("top", "pot"));
	}
	
	public static boolean isAnagram(String word, String anagram){
		if(word.length()!=anagram.length()){
			return false;
		}
		char[] wordCharArray = word.toCharArray();
		char[] anagramCharArray = anagram.toCharArray();
		sort(wordCharArray);
		sort(anagramCharArray);
		return equals(wordCharArray, anagramCharArray);
	}
	
	public static void sort(char[] charArray){
		for(int i=0; i < charArray.length; i++){
			for(int j=0; j<charArray.length; j++){
				if(charArray[i] > charArray[j]){
					char temp = charArray[i];
					charArray[i] = charArray[j];
					charArray[j] = temp;
				}
			}
		}
	}
	
	public static boolean equals(char[] a, char[] b){
		if(a.length != b.length){
			return false;
		}
		for(int i=0; i<a.length; i++){
			if(a[i] != b[i]){
				return false;
			}
		}
		return true;
	}
}
