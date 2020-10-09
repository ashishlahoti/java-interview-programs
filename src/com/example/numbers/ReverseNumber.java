package com.example.numbers;

public class ReverseNumber {

	public static void main(String[] args) {
		System.out.println(reverse(123));
		System.out.println(reverse(0));
		System.out.println(reverse(2));
		System.out.println(reverse(-123));
		System.out.println(reverse(1534236469));
	}
	
	public static int reverse(int x) {
        long rev = 0;
        while(x != 0){
        	rev = rev*10 + x%10;
            x = x/10;
        }
        if(rev < Integer.MIN_VALUE || rev > Integer.MAX_VALUE) {
        	return 0;
        }
        return (int)rev;
    }
}
