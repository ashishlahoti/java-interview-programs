package com.example.core;
import java.util.HashMap;
import java.util.Map;


public class PalindromLengthPuzzle {

	public static void main(String[] args){
		PalindromLengthPuzzle.getLength(new String[]{"Bharti", "Brijesh", "Akash", "Bhavya", "Chand", "Brijesh", "Arvind", "Bhavna"});
		PalindromLengthPuzzle.getLength(new String[]{"Bharti", "Brijesh", "Bhavya", "Brijesh", "Bhavna"});
		PalindromLengthPuzzle.getLength(new String[]{"Bharti", "Brijesh", "Chand", "Brijesh", "Bhavna"});
		PalindromLengthPuzzle.getLength(new String[]{"Bharti", "Bharat", "Akash", "Bhavya", "Chand", "Brijesh", "Chetak", "Arvind", "Bhavna"});
	}
	
	public static int getLength(String[] input1){
		int length = 0;
		Map<String, Integer> counterMap = new HashMap<String, Integer>(); 
		
		for(String s : input1){
			String charValue = s.charAt(0) +"";
			Integer counter = counterMap.get(charValue);
			
			if(counter != null){
				counterMap.put(charValue, counter + 1);
			}else{
				counterMap.put(charValue, 1);
			}
		}
		
		for(String s : counterMap.keySet()){
			int count = counterMap.get(s);
			if(count % 2 == 0){
				length = length + count;
				counterMap.put(s, 0);
			}else if(count > 2){
				length = length + count - 1;
				counterMap.put(s, 1);
			}
		}
		
		for(String s : counterMap.keySet()){
			int count = counterMap.get(s);
			if(count == 1){
				length = length + count;
				break;
			}
		}
		System.out.println(length);
		return length;
	}
	
}
