package com.example.exception;

final class StackOverflowError {

	public static void main(String[] args){
		StackOverflowError.go();
	}
	
	public static void go(){
		go();
	}
}
