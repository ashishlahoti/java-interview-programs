package com.example.algo.twopointer;

public class IsSubsequence {

    public static void main(String[] args) {
        System.out.println(isSubsequence("abc", "ahbgdc")); //true
        System.out.println(isSubsequence("axc", "ahbgdc")); //false
        System.out.println(isSubsequence("axc", "ahbgdc")); //false
    }
    public static boolean isSubsequence(String s, String t) {
        int i  = 0;
        int j = 0;

        while(i < s.length() && j < t.length()){
            if(s.charAt(i) == t.charAt(j)){
                i++;
                j++;
            }else{
                j++;
            }
        }
        return i == s.length();
    }
}
