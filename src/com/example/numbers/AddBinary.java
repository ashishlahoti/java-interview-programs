package com.example.numbers;

public class AddBinary {
	
	public static void main(String[] args) {
		System.out.println(addBinary("1010", "1011")); //10101
		System.out.println(addBinary("11", "1")); //100
		System.out.println(addBinary("1111", "1111"));
	}
	
	public static String addBinary(String a, String b) {
        int carry = 0;
        String str = "";
        int i = a.length()-1;
        int j = b.length()-1;
        int sum = 0;
        while(i>=0 && j>=0){
            int aNum = a.charAt(i) -'0';
            int bNum = b.charAt(j) -'0';
            sum = carry + aNum + bNum;
            if(sum == 3){
                carry = 1;
                str = "1" + str;
            }else if(sum == 2){
                carry = 1;
                str = "0" + str;
            }else if(sum == 1){
                carry = 0;
                str = "1" + str;
            }else{
                carry = 0;
                str = "0" +str;
            }
            i--;
            j--;
        }
        while(i>=0){
        	sum = carry + (a.charAt(i)-'0');
        	if(sum > 1){
                carry = sum-1;
                str = "0" + str;
            }else if(sum == 1){
                carry = 0;
                str = "1" + str;
            }else{
                carry = 0;
                str = "0" +str;
            }
            i--;
        }
        while(j>=0){
        	sum = carry + (b.charAt(j)-'0');
        	if(sum > 1){
                carry = sum-1;
                str = "0" + str;
            }else if(sum == 1){
                carry = 0;
                str = "1" + str;
            }else{
                carry = 0;
                str = "0" +str;
            }
            j--;
        }
        
        if(carry == 1){
        	return "1" + str;
        }else {
        	return str;
        }      
    }
}
