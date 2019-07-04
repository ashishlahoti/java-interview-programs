package com.techgig;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args){
		//print(separatersInString("abc((d(ef)hij{klmn}opq[rst]uvw"));
		//System.out.println(subsetToTarget(new int[]{1,2,3,4,5}, 5));
		//System.out.println(subsetToTarget(new int[]{3,5,7,8}, 8));
		//System.out.println(subsetToTarget(new int[]{5,1,2,3,4,5}, 5));
		//System.out.println(subsetToTarget(new int[]{5,5,5,5,5,5}, 5));
		//System.out.println(subsetToTarget(new int[]{2,2,2,2,2,2}, 1));
		//print(unityInZeros(new int[]{1, 2, 3, 0, 4, 0 , 5}));
		//System.out.println(steppingNumber(12345));
		//System.out.println(steppingNumber(543132345));
		//System.out.println(repeatedString("abcabc"));
		largestsquarehouse(new int[][]{{1,1,1,0,0},{1,1,1,0,0},{0,1,0,1,0},{0,0,1,1,1}});
	}
	
	public static String[] separatersInString(String input1){
	    List<String> splits = new ArrayList<>();
	    int startIndex = 0;
	    int skipIndex = 0;
		for(int i=startIndex; i<input1.length(); i++){
			if(input1.charAt(i)== '(' || input1.charAt(i) == ')' || input1.charAt(i) == '{' || input1.charAt(i) == '}' || input1.charAt(i) == '[' || input1.charAt(i) == ']'){
				if(i < input1.length()-1 && input1.charAt(i) == input1.charAt(i+1)){
					skipIndex = i;
					i++;
				}else{
					if(skipIndex > 0 && skipIndex > startIndex && skipIndex < i){
						splits.add(input1.substring(startIndex, skipIndex) + input1.substring(skipIndex+1, i));
					}else{
						splits.add(input1.substring(startIndex, i));
					}
					startIndex = i+1;
				}
			}else if(i==input1.length()-1){
				splits.add(input1.substring(startIndex, i+1));
			}
		}
	    return splits.toArray(new String[0]);
    }
	
	public static int subsetToTarget(int[] input1,int input2){
		int count = 0;
		boolean singleDigitCount = false;
		for(int i=0; i<input1.length; i++){
			if(input1[i] == input2){
				singleDigitCount = true;
			}
			for(int j=i+1; j<input1.length; j++){
				if(input1[i] + input1[j] == input2){
					count++;
				}
			}
		}
	    return count + (singleDigitCount ? 1 : 0);
    }
	
	public static <T> void print(T[] array){
		System.out.print("Array: ");
		for(T s: array){
			System.out.print(s+", ");
		}
	}
	
	public static void print(int[] array){
		System.out.print("Array: ");
		for(int s: array){
			System.out.print(s+", ");
		}
	}
	
	public static int[] unityInZeros(int[] input1)
    {
	   for(int i=0; i<input1.length; i++){
		   if(input1[i] == 0){
			   for(int j=i; j <input1.length-1; j++){
				   input1[j] = input1[j+1];
			   }
			   input1[input1.length-1] = 0;
		   }
	   }
	   return input1;
    }
	
	public static int steppingNumber(int input1)
    {	
		for(int num=input1,rem=-1; num > 0; num = num/10){
			if(rem > 0 && Math.abs(num%10-rem) != 1){
				return -1;
			}
			if(num%10 == 0){
				return -1;
			}
			rem = num%10;
			System.out.println(rem +"#");
		}
	    return 1;
    }
	
	public static int repeatedString(String input1)
    {
	    for(int i=2; i<input1.length(); i++){
	    	if(input1.length()%i == 0){
	    		int mid = input1.length()/i;
	    		for(int j=mid,startIndex=0; j<= input1.length(); j = mid+mid, startIndex=mid){
	    			if(!input1.substring(startIndex, j).equals(input1.substring(j, j+mid))){
	    				break;
	    			}else if(j+mid==input1.length()){
	    				return 1;
	    			}
	    		}
	    	}
	    }
	    return -1;
    }
	
	public static String largestsquarehouse(int[][] input1)
    {
		int row = input1.length;
		int column = input1[0].length;
		for(int i=0; i<row; i++){
			for(int j=0; j<column; j++){
				if(input1[i][j] == 0){
					int colCount = 0;
					for(int k=j; k<column;k++){
						if(input1[i][k]==0){
							colCount++;
						}else{
							break;
						}
					}
					int rowCount =0;
					for(int l=i;l<row;l++){
						if(input1[l][j]==0){
							rowCount++;
						}else{
							break;
						}
					}
					System.out.println(colCount+"#"+rowCount);
				}
			}
		}
	    return null;
    }
}
