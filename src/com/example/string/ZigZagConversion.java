package com.example.string;

/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this: 
 * (you may want to display this pattern in a fixed font for better legibility)
 *
 *	P   A   H   N
 *	A P L S I I G
 *	Y   I   R
 *
 *	P     I    N
	A   L S  I G
	Y A   H R
	P     I
 */
public class ZigZagConversion {

	public static void main(String[] args) {
		System.out.println(convert("PAYPALISHIRING", 3)); //PAHNAPLSIIGYIR
		System.out.println(convert("PAYPALISHIRING", 4)); //PINALSIGYAHRPI
	}
	public static String convert(String s, int numRows) {
		String c = "";
		for(int i=0; i< numRows; i++) {
			for(int j=0; j+i<s.length(); j=j+2*numRows-2){
	            c += s.charAt(i+j);
	            if(i !=0 && i != numRows-1 && (j + 2*numRows-2 - i) < s.length()) {
	            	c += s.charAt(j+2*numRows-2-i);
	            }
	        }
		}
        return c;
    }
}
