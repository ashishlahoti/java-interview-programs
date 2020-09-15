package com.example.collection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IterateOverList {

	public static void main(String[] args) {
		
		
		List<Integer> primeNumbers = Arrays.asList(1, 2, 3, 5, 7);
		
		// basic for loop
		for(int i = 0; i < primeNumbers.size(); i++) {
			System.out.println(primeNumbers.get(i));
		}
		
		// enhanced for loop
		for (Integer number : primeNumbers) {
		    System.out.println(number); 
		}
		
		// Iterable.forEach with lambda expression
		primeNumbers.forEach(number -> System.out.println(number));
		
		// Iterable.forEach with method reference ::
		primeNumbers.forEach(System.out::println);
		
		// Stream.forEach with lambda expression
		primeNumbers.stream().forEach(number -> System.out.println(number));
		
		// Stream.forEach with method reference ::
		primeNumbers.stream().forEach(System.out::println);
		
		
		Iterator<Integer> iterator = primeNumbers.iterator();
		
		 
		while(iterator.hasNext()) {
		    System.out.println(iterator.next()); 
		}
		
		ListIterator<Integer> listIterator = primeNumbers.listIterator();
		 
		while(listIterator.hasNext()) {
		    System.out.println(listIterator.next());
		}
		
		while(listIterator.hasPrevious()) {
		    System.out.println(listIterator.previous());
		}

	}
}
