package com.example.string;

public class FindDuplicatesInString {

	public static void main(String[] args){
		FindDuplicatesInString.printDuplicate("sadsasadsadc");
	}
	
	private static void printDuplicate(String s){
		int space = 0;
		if(s == null || s.length() == 0 ){
			return;
		}
		
		for(int i=0; i < s.length(); i++){
			int ch = s.charAt(i);
			int index = ch-97;
			if(isBitSet(space, index)){
				System.out.println((char) (ch));
			}
			space = setBit(space, index);
		}
	}
	
	private static int setBit(int num, int pos){
		return num | 1 << pos;
	}
	
	private static boolean isBitSet(int num, int pos){
		return (num & 1 << pos)!=0;
	}
}
