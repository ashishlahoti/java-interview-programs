package com.example.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MutableObject {

	public static void main(String[] args) {
		
		Person person1 = new Person();
		person1.setName("Adam");
		
		Person person2 = new Person();
		person2.setName("Ben");
		
		Set<Person> setOfPerson = new HashSet<>(Arrays.asList(person1, person2));
		System.out.println(setOfPerson);
		
		person1.setName("Charlie");
		System.out.println(setOfPerson);
		
		
	}
}

class Person {
	
	String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Person { name: " + name + " }";
	}
}
