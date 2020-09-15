package com.example.string;

public class CompareString {

	public static void main(String[] args) {
		
		String string1 = "CodingNConcepts";
		String string2 = "CodingNConcepts";
		String string3 = new String("CodingNConcepts");
		String string4 = new String("CodingNConcepts");
		
		// == operator
		System.out.println(string1 == string2);
		System.out.println(string1 == string3);
		System.out.println(string3 == string4);
		
		string3 = string3.intern();
		System.out.println(string1 == string3);
		System.out.println(string3 == string4);
		// equals
		//System.out.println(string1.equals(string2));
		//System.out.println(string1.equals(string3));
		//System.out.println(string3.equals(string4));
		//System.out.println(string1.equals(null));
		
		
	}
}
