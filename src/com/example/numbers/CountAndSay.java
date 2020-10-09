package com.example.numbers;

public class CountAndSay {
	public static void main(String[] args) {
		System.out.println(countAndSay(1));
		System.out.println(countAndSay(2));
		System.out.println(countAndSay(3));
		System.out.println(countAndSay(4));
		System.out.println(countAndSay(5));
		System.out.println(countAndSay(6));
		System.out.println(countAndSay(7));
		System.out.println(countAndSay(8));
		System.out.println(countAndSay(9));
		System.out.println(countAndSay(10));
	}
	public static String countAndSay(int n) {
        if(n==1){
            return "1";
        }else if(n==2){
            return "11";
        }
        
        String countAndSay = "11";
        for(int i=3; i<= n; i++){
            int count = 1;
            String s = "";
            for(int j=1; j<countAndSay.length(); j++){
                if(countAndSay.charAt(j) == countAndSay.charAt(j-1)){
                    count = count +1;
                    if(j+1 == countAndSay.length()) {
                    	s = s + count + countAndSay.charAt(j-1);
                    }
                }else{
                    s = s + count + countAndSay.charAt(j-1);
                    count = 1;
                    if(j+1 == countAndSay.length()) {
                    	s = s + count + countAndSay.charAt(j);
                    }                   
                }
            }
            countAndSay = s;
        }
        return countAndSay;
    }
}
