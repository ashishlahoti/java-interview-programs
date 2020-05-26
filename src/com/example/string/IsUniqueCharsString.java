package com.example.string;
/**
 * Find if all the characters are unique in given string without using any data structure.
 * @author aklahoti
 *
 */
public class IsUniqueCharsString {

	public static void main(String[] args){
		isUniqueBruteForce("abcd");
		isUnique("abcd");
	}
	
	//Iterating two loops - runtime complexity O(N SQUARE)
	public static boolean isUniqueBruteForce(String s){
		//If String is ASCII there can be 128 unique chars
		if(s.length() > 128) return false;
		
		for(int i=0; i <s.length(); i++){
			for (int j = i+1; j < s.length(); j++) {
				if(s.charAt(i) == s.charAt(j)){
					System.out.println(s + " is not unique");
					return false;
				}
			}
		}
		System.out.println(s + " is unique");
		return true;
	}
	
	public static boolean isUnique(String s){
		//If String is ASCII there can be 128 unique chars
		if(s.length() > 128) return false;
		int space = 0;
		for(int i=0; i <s.length(); i++){
			int pos = s.charAt(i);
			if(isBitSet(space, pos)){
				System.out.println(s + " is not unique");
				return false;
			}
			space = setBit(space, pos);
		}
		System.out.println(s + " is unique");
		return true;
	}

	
	public static int setBit(int val, int index){
		return val | 1 << index;
	}
	
	public static boolean isBitSet(int val, int index){
		return (val & 1 << index) != 0;
	}
}
