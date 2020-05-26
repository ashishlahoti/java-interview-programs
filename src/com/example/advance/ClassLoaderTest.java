package com.example.advance;

public class ClassLoaderTest extends ClassLoader{

	public static void main(String[] args) {
		System.out.println(java.lang.String.class.getClassLoader());
		System.out.println(ClassLoaderTest.class.getClassLoader());
	}
}
