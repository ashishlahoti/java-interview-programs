package com.example.dto;

public class Circle extends Shape{

	public void draw(){
		System.out.println("Circle is drawn");
	}
	
	public void throwException() throws RuntimeException{
		throw new RuntimeException();
	}
}
