package com.example.string;

import java.util.LinkedHashMap;
import java.util.Map;

public class BalancedString {

	public static void main(String[] args){
		//System.out.println(balancedStringOrNot("azAZ<>{}[]"));
		//print(countForSurpasser(new int[]{7,2,7,5,3,0,8,1}));
		//print(countForSurpasser(new int[]{5,4,3,2,1}));
		//print(countForSurpasser(new int[]{1, 2, 3, 4, 5}));
		//print(countForSurpasser(new int[]{1,1,1,1,1}));
		System.out.println(FirstUnrepeatedCharacter("aasswwqqeebbccdde"));
	}
	
	public static int balancedStringOrNot(String input1)
    {
		int bracket[] = new int[3];
		for(int i=0;i <input1.length(); i ++){
			int input = input1.charAt(i);
			if(input == 60){
				bracket[0]++;
			}else if(input == 62){
				bracket[0]--;
			}else if(input == 123){
				bracket[1]++;
			}else if(input == 125){
				bracket[1]--;
			}else if(input == 91){
				bracket[2]++;
			}else if(input == 93){
				bracket[2]--;
			}
			System.out.println(input +":"+ input1.charAt(i));
		}
		for(int i : bracket){
			if(i != 0){
				return -1;
			}
		}
	   return 1;
    }
	
	 public static int[] countForSurpasser(int[] input1){
		   int[] surpasser = new int[input1.length];
		   for(int k=0; k < input1.length; k++){
			   surpasser[k] = 0;
		   }
		   for(int i=0; i < input1.length; i++){
			   for(int j=i+1; j < input1.length; j++){
				   if(input1[j] > input1[i]){
					   surpasser[i]++;
				   }
			   }
		   }
		   return surpasser;
	 }
	 
	 public static void print(int[] input){
		 for(int i : input){
			 System.out.println(i);
		 }
		 System.out.println("==========================");
	 }
	 
	 public static String FirstUnrepeatedCharacter(String input1) {
	        Map<String, Integer> map = new LinkedHashMap<>();
		    for(int i=0; i < input1.length(); i++){
		    	String char1 = input1.charAt(i)+"";
		    	if(map.containsKey(char1)){
		    		map.put(char1, (map.get(char1))+1);
		    	}else{
		    		 map.put(char1 , 1);
		    	}
		    }
		    for(int j=0; j < input1.length(); j++){
		    	String char2 = input1.charAt(j)+"";
		    	if(map.containsKey(char2) && map.get(char2) == 1){
		    		return char2;
		    	}
		    }
		    return "";
	    }
}
