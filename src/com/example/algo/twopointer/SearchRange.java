package com.example.algo.twopointer;

import java.util.Arrays;

/**
 * Find the first and last occurrence index of target in the given Array in O(logN) complexity
 */
public class SearchRange {

    public static void main(String[] args) {
        System.out.print(Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8)));
        System.out.print(Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6)));
        System.out.print(Arrays.toString(searchRange(new int[]{}, 6)));
    }

    /**
     * Use binary search to find the first and last occurrence in O(logN)
     */
    public static int[] searchRange(int[] nums, int target) {
        int left = 0;
        int right = nums.length-1;
        int mid = left + (right-left)/2;

        while(left <= right) {
            int val = nums[mid];
            if(val < target){
                left = mid+1;
                mid = left + (right-left)/2;
            }else if(val > target){
                right = mid-1;
                mid = left + (right-left)/2;
            }else {
                // assign the target index to both left and right
                left = right = mid;
                // traverse left to find first occurrence of target
                while(left > 0 && nums[left-1] == target){
                    left--;
                }
                // traverse right to find last occurrence of target
                while(right < nums.length-1 && nums[right+1] == target){
                    right++;
                }
                return new int[]{left, right};
            }
        }
        return new int[]{-1, -1};
    }
}
