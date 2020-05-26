package com.example.designpattern;

public class TestPattern {
	public static void main(String[] args){
		Singleton singletonPattern = Singleton.getInstance();
		singletonPattern.print();
	}
}
