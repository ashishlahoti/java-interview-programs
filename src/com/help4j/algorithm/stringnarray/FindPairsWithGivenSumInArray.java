package com.help4j.algorithm.stringnarray;

import java.util.HashMap;
import java.util.Map;

public class FindPairsWithGivenSumInArray {

	public static void main(String[] args) {
		findPairs(new int[]{1, 2, 3, 5, 6, 7}, 8);
	}
	public static void findPairs(int[] array, int sum){
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < array.length; i++) {
			if(map.containsKey(array[i])){
				System.out.println(array[i] +" + " + map.get(array[i]) );
			}else{
				map.put(sum-array[i], array[i]);
			}
		}
	}
}

