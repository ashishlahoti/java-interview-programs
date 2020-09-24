package com.example.array;

public class RemoveElements {
	public static void main(String[] args) {
		System.out.println(removeElement(new int[] {3, 2, 2, 3}, 3));
		
	}

	public static int removeElement(int[] nums, int val) {
        int cursor = 0;
        for(int i = 0; i< nums.length; i ++){
            if(nums[i] != val){
                nums[cursor] = nums[i];
                cursor++;
            }
        }
        return cursor;
    }
}
