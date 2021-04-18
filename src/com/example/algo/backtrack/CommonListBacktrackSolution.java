package com.example.algo.backtrack;

import java.util.ArrayList;
import java.util.List;

public class CommonListBacktrackSolution {

	public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> lists = new ArrayList<>();
        subsets(lists, new ArrayList<Integer>(), nums, 0);
        return lists;
    }
    
    public void subsets(List<List<Integer>> lists, List<Integer> list, int[] nums, int start) {
        lists.add(new ArrayList<>(list));       
        for(int i=start; i<nums.length; i++){
            list.add(nums[i]);
            subsets(lists, list, nums, i+1);
            list.remove(list.size()-1);
        }        
    }
}
