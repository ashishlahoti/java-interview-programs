package com.example.string;

import java.util.Arrays;

public class LongestNiceSubstring {
    public static void main(String[] args) {
        System.out.println(longestNiceSubstring("YazaAay"));
        System.out.println(longestNiceSubstring("Bb"));
        System.out.println(longestNiceSubstring("c"));
        System.out.println(longestNiceSubstring("dDzeE"));
    }

    public static String longestNiceSubstring(String s) {
        if (s == null || s.length() < 2) return "";
        String longestString = "";
        for (int i = 0; i < s.length(); i++) {
            for (int j = i+1; j < s.length(); j++) {
                String sub = s.substring(i, j+1);

                if (isNiceString(sub)) {
                    if (sub.length() > longestString.length()) {
                        //System.out.println(sub);
                        longestString = sub;
                    }
                }
            }
        }
        return longestString;
    }

    public static boolean isNiceString(String s) {
        int[] uppercase = new int[26];
        int[] lowercase = new int[26];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                lowercase[c - 'a']++;
            } else {
                uppercase[c - 'A']++;
            }
        }
        //System.out.println(Arrays.toString(uppercase));
        //System.out.println(Arrays.toString(lowercase));
        for (int j = 0; j < uppercase.length; j++) {
            int x = uppercase[j];
            int y = lowercase[j];
            if ((x == 0 && y > 0) || (y == 0 && x > 0)) {
                return false;
            }
        }
        return true;

    }
}
