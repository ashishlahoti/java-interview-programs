package com.example.array;

import java.util.Arrays;

public class MergeTwoSortedArrays {

	public static void main(String[] args) {
		System.out.println(findMedianSortedArrays(new int[] {1,3}, new int[] {2}));
		System.out.println(findMedianSortedArrays(new int[] {1,2}, new int[] {3,4}));
		System.out.println(findMedianSortedArrays(new int[] {0,0}, new int[] {0,0}));
		System.out.println(findMedianSortedArrays(new int[] {}, new int[] {1}));
		System.out.println(findMedianSortedArrays(new int[] {2}, new int[] {}));
		
		
		int num1[] = new int[] {1, 10, 12, 13, 14, 22, 23};
		int num2[] = new int[] {5, 18, 20};
		mergeTwoSortedArraysWithoutUsingSpace(num1, num2);
		System.out.println(Arrays.toString(num1));
		System.out.println(Arrays.toString(num2));
	}
	
	public static int[] mergeTwoSortedArraysWithoutUsingSpace(int[] nums1, int[] nums2) {
		int m = nums1.length;
        int n= nums2.length;
        int[] mergedNums = new int[m+n];
        int i=0;
        int j=0;
        while(i<m) {
        	if(nums1[i] < nums2[j]) {    		
        		i++;
        	}else {
        		swap(nums1, nums2, i, j);
        		Arrays.sort(nums2);
        		i++;
        	}
        }
        return mergedNums;
	}
	
	public static int[] mergeTwoSortedArrays(int[] nums1, int[] nums2) {
		int m = nums1.length;
        int n= nums2.length;
        int[] mergedNums = new int[m+n];
        int i=0, j=0, k=0;
        while(i<m && j<n) {
        	if(nums1[i] < nums2[j]) {
        		mergedNums[k] = nums1[i];
        		i++;
        		k++;
        	}else {
        		mergedNums[k] = nums2[j];
        		j++;
        		k++;
        	}
        }
        while(i<m) {
        	mergedNums[k] = nums1[i];
        	i++;
        	k++;
        }
        while(j<n) {
        	mergedNums[k] = nums2[j];
        	j++;
        	k++;
        }
        return mergedNums;
	}
	
	public static void swap(int[] nums1, int[] nums2, int i, int j) {
		int temp = nums1[i];
		nums1[i] = nums2[j];
		nums2[j] = temp;
	}
	
	public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] mergedNums = mergeTwoSortedArrays(nums1, nums2);
        int k = mergedNums.length;
        if(k%2 != 0) {
        	return mergedNums[(k-1)/2];
        }else {
        	return (mergedNums[k/2] + mergedNums[(k/2)-1])/2d;
        }
    }
}
