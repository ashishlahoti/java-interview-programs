package com.example.array;

import java.util.HashSet;
import java.util.Set;

public class ContainsDuplicate {
	public static void main(String[] args) {
		System.out.println(containsDuplicate(new int[] {1, 1, 2, 2}));
		System.out.println(containsDuplicate(new int[] {1, 2, 3}));
	}
	
	public static boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for(int i=0; i<nums.length; i++){
            if(!set.add(nums[i])){
            	return true;
            }                
        }
        return false;
    }
}
