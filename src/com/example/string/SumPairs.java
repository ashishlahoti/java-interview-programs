package com.example.string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SumPairs {
	
	public static void main(String[] args){
		printPairs(new int[]{9, 3, 6, 4, 7, 5, 8, 2}, 10);
		printTriplets(new int[]{9, 3, 6, 4, 7, 5, 8, 2}, 10);
	}
	
	public static void printTriplets(int[] array, int sum){
		for(int i=0; i<array.length; i++){
			System.out.println(array[i]  + "#");
			printPairs(array, sum-array[i], i);
		}
	}
	
	public static void printPairs(int[] array, int sum, int index){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i=0; i<array.length; i++){
			if(i==index){
				continue;
			}
			if(map.containsKey(array[i])){
				System.out.println(array[i] + " + " + map.get(array[i]) + " = " +sum);
			}else{
				map.put(sum-array[i], array[i]);
			}
		}
	}
	
	/*O(n)*/
	public static void printPairs(int[] array, int sum){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i=0; i<array.length; i++){
			if(map.containsKey(array[i])){
				System.out.println(array[i] + " + " + map.get(array[i]) + " = " +sum);
			}else{
				map.put(sum-array[i], array[i]);
			}
		}
	}
}
