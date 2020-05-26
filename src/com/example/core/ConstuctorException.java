package com.example.core;

public class ConstuctorException {

	{ 
		if(true) {
			throw new Exception();
		}
	}
	
	public ConstuctorException() throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		new ConstuctorException();
	}
}
