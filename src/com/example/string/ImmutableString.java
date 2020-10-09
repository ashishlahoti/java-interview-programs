package com.example.string;

public class ImmutableString {

	public static void main(String[] args) {
		String s1 = new String("CodingNConcepts");
		String s2 = s1.toUpperCase();
		System.out.println(s1 == s2);  //false
		s1 = s2;
		System.out.println(s1 == s2);  //true
		
		
		// String Literal
		  String a = "abc"; 
		  String b = "abc";
		  System.out.println(a == b);  // true

		  // String Object
		  String c = new String("abc");
		  String d = new String("abc");
		  System.out.println(c == d);  // false
		
		  System.out.println(c.intern() == d.intern()); 
	}
}
