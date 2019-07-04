package com.techgig.test;

public class ChildrenCoinGcdProbleam {

	public static void main(String[] args){
		int[] output = coins_value(new int[]{5, 13});
		System.out.println(output[0] +","+ output[1]);
		System.out.println(gcd(6, 8));
	}
	public static int[] coins_value(int[] input1)
    {
		int[] output = {0, 0};
		
		if(input1 == null || input1.length != 2){
			return output;
		}
		
		int a = input1[0];
		int b = input1[1];
		
		if((a < 0 || a > 1000) && (b < 0 || b > 1000)){
			return output;
		}
		
		int x = 0;
		int y = 0;
		if(a == b){
			x = 2;
			y = -1;
		}else if(a > b){
			x = -1;
			y = 1;
			while(a*x + b*y < 0 && a*x + b*y < b){
				y++;
			}
		}else{
			y = -1;
			x = 1;
			while(a*x + b*y < 0 && a*x + b*y < a){
				x++;
			}
		}
		output[0] = x;
		output[1] = y;
        return output;
    }
	
	public static int gcd(int a, int b){
		int big = (a >= b) ? a : b;
		int small = (a >= b ) ? b : a;
		int remainder = big%small;
		int gcd = small;
		while(remainder != 0){
			gcd = remainder;
			remainder = small%remainder;
			
		}
		return gcd;
	}
}
