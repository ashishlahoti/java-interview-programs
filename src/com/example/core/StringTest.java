package com.example.core;

public class StringTest {

	public static void main(String[] args) {
		String a = "Hello";
		String b = "Hello";
		String c = new String("Hello");
		String d = new String("Hello");
		
		System.out.println(a == b);
		System.out.println(c == d);
		System.out.println(a == d.intern());
		System.out.println(c.intern() == d.intern());
		
		Object o = new Object();
		
	}
}
