package com.example.designpattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestPattern {
	public static void main(String[] args) throws ParseException{
		//Singleton singletonPattern = Singleton.getInstance();
		//singletonPattern.print();
		
		ImmutablePerson person1 = new ImmutablePerson("Adam", 68, new SimpleDateFormat("dd-MM-yyyy").parse("22-08-1986"));

		ImmutablePerson person2 = new ImmutablePerson("Ben", 52, new SimpleDateFormat("dd-MM-yyyy").parse("01-01-1990"));

		Set<ImmutablePerson> setOfPerson = new HashSet<>(Arrays.asList(person1, person2));
		System.out.println(setOfPerson);

		//person1.setName("Charlie");
		//System.out.println(setOfPerson);
		
		
		Date person1Date = person1.getDateOfBirth();
		person1Date.setYear(2020);
		System.out.println(setOfPerson);
		
		
	}
}
