package com.example.core.fp;

@FunctionalInterface
public interface Java8Interface1 {

	void execute();
	
	default void print(String s){
		System.out.println(s);
	}
	
	static boolean isNull(String s){
		return s == null || "".equals(s);
	}
}
