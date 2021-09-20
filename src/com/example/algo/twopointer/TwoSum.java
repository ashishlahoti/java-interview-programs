package com.example.algo.twopointer;

import java.util.*;

public class TwoSum {
    public static void main(String[] args) {
        int[] numbers = new int[] { -3, 2, 3, 3, 6, 8, 15 };
        System.out.println(twoSum_getElements(numbers, 6));
        System.out.println(twoSum_getIndex(numbers, 6));
    }

    public static List<Integer> twoSum_getIndex(int[] numbers, int target){
        Map<Integer, Integer> map = new HashMap<>();
        for(int i=0; i< numbers.length; i++){
            map.put(numbers[i], i);
        }

        for(int i=0; i< numbers.length; i++){
            int remaining = target - numbers[i];
            if(map.containsKey(remaining) && map.get(remaining) != i){
                return new ArrayList<>(Arrays.asList(i, map.get(remaining)));
            }
        }
        return new ArrayList<>();
    }

    public static List<Integer> twoSum_getElements(int[] numbers, int target) {
        Arrays.sort(numbers);
        int start = 0;
        int end = numbers.length - 1;
        while (start < end) {
            int total = numbers[start] + numbers[end];
            if (total == target) {
                return new ArrayList<>(Arrays.asList(numbers[start], numbers[end]));
            } else if (total > target) {
                end--;
            } else {
                start++;
            }
        }
        return new ArrayList<>();
    }
}
