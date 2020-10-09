package com.example.string;

public class MultiplyString {
	public static void main(String[] args) {
		//System.out.println(sum("121", "12"));
		//System.out.println(multiply("123", "456"));
		System.out.println(multiply("0", "0"));
		//System.out.println(trimPrefixZeros("0056088"));
	}

	public static String multiply(String num1, String num2) {
        String sum = "";
 
        for(int i=num1.length()-1,k=0; i>=0; i--,k++){
            int carry = 0;
        	String mult = "";       	
        	for(int j=num2.length()-1; j>=0; j--) {
        		int n1 = i>=0 ? num1.charAt(i) - '0' : 0;
    			int n2 = j>=0 ? num2.charAt(j) - '0' : 0;
                //System.out.println(n1 + "*" + n2);
                int multiply = n1*n2 + carry;
                int val = multiply%10;
                carry = multiply/10;
                mult = val + mult;
        	}
        	mult = carry + mult;
        	for(int l=0; l<k;l++) {
        		mult = mult + '0';
        	}
        	sum = sum(mult, sum);          
        }
        return trimPrefixZeros(sum);
    }
	
	public static String sum(String num1, String num2) {
		int carry = 0;
		String s = "";
		for(int i= num1.length()-1, j=num2.length()-1; i>=0 || j>=100; i--,j--) {
			int n1 = i>=0 ? num1.charAt(i) - '0' : 0;
			int n2 = j>=0 ? num2.charAt(j) - '0' : 0;
			int sum = n1+n2+carry;
			carry =sum/10;
			s = sum%10 + s;
		}
		return carry + s;
	}
	
	public static String trimPrefixZeros(String num) {
		String s = "";
		boolean isPrefixZeros = true;
		for(int i=0; i< num.length(); i++) {
			if(num.charAt(i) != '0' || !isPrefixZeros) {
				s = s + num.charAt(i);
				isPrefixZeros = false;
			}
		}
		if(s == "") {
			return "0";
		}
		return s;
	}
}
