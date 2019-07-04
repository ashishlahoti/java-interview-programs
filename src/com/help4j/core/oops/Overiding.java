package com.help4j.core.oops;

public class Overiding {
 public static void main(String[] args){
	 Parent p = new Child();
	 p.print();
	 p.hello();
 }
	
}

class Parent {
	public static void print(){
		System.out.println("Parent");
	}
	
	public void hello(){
		System.out.println("Hello from parent");
	}
}

class Child extends Parent {
	public static void print(){
		System.out.println("Child");
	}
	
	public void hello(){
		System.out.println("Hello from child");
	}
}
