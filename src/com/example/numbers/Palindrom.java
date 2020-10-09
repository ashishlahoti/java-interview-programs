package com.example.numbers;

public class Palindrom {

	public static void main(String[] args) {
		System.out.println(isPalindrome(121));
	}
	
	public static boolean isPalindrome(int x) {
        if(x<0){
            return false;
        }
        int y = 0;
        int z = x;
        while(x>0){
            y = y*10+x%10;
            x = x/10;
        }
        return z == y;
    }
}
