package com.example.algo.twopointer;

/**
 * Determine if the string is Palindrome. Ignore space and special chars
 */
public class isPalindromeString {
	
	public static void main(String[] args) {
		System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
		System.out.println(isPalindrome("race a car"));
		System.out.println(isPalindrome(".,"));
		System.out.println(isPalindrome("0P"));
	}

	public static boolean isPalindrome(String s) {
        s = s.toLowerCase();
        int i = 0;
        int j = s.length()-1;
        
        while(i < j){
            char left = s.charAt(i);
            char right = s.charAt(j);
            while(!((left >= 'a' && left <= 'z') || (left >= '0' && left <= '9')) && i<j){
                left = s.charAt(++i);
            }
            while(!((right >= 'a' && right <= 'z') || (right >= '0' && right <= '9')) && j>i){
            	right = s.charAt(--j);
            }
            if(left != right){
                return false;
            }else{
                i++;
                j--;
            }
        }
        return true;
    }
}
