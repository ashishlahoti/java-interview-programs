package com.example.algo.twopointer;

import java.util.Arrays;

public class ThreeSumClosest {
	public static void main(String[] args) {
		System.out.println(threeSumClosest(new int[] {0,2,1,-3}, 1));
	}

	public static int threeSumClosest(int[] nums, int target) {
		Arrays.sort(nums);
        int closest = Integer.MAX_VALUE;
        int closestSum = 0;
        for(int i=0; i<nums.length-2; i++){
        	int j=i+1;
        	int k = nums.length-1;
        	while(j<k) {
        		int sum = nums[i] + nums[j] + nums[k];
        		if(sum >= target) {
        			k--;
        		}else {
        			j++;
        		}
        		if(Math.abs(target-sum) < closest){
                    closest = Math.abs(target-sum);
                    closestSum = sum;
                }
        	}          
        }
        return closestSum;
    }
}
