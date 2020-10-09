package com.example.array;

/**
 * Find smallest positive integer which does not occur in the sequence
 */
public class FirstMissingPositive {
	
	public static void main(String[] args) {
		//System.out.println(firstMissingPositive(new int[] {1, 3, 6, 4, 1, 2}));
		System.out.println(firstMissingPositive(new int[] {1}));
	}

	public static int firstMissingPositive(int[] nums) {
		int i;
        for(i=1; i<=nums.length; i++){
            boolean isMissing = true;
            for(int j=0; j<nums.length; j++){
                if(i == nums[j]){
                	isMissing = false;
                    break;
                }
            }
            if(isMissing){
                return i;
            }
        }
        return i;
    }
}
