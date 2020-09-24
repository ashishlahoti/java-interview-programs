package com.example.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InitializeListWithValues {

	//private static T[] elements;

	public static void main(String[] args) {

		/**
		 * making an anonymous inner class with an instance initializer (also known as
		 * an "double brace initialization"): However, I'm not too fond of that method
		 * because what you end up with is a subclass of ArrayList which has an instance
		 * initializer, and that class is created just to create one object -- that just
		 * seems like a little bit overkill to me.
		 */
		List<String> arraylist1 = new ArrayList<String>() {
			{
				add("A");
				add("B");
				add("C");
			}
		};
		
		List<String> arraylist2 = new ArrayList<>(Arrays.asList("foo", "bar"));
		arraylist2.add("baz");
		
		List<String> arraylist3 = new ArrayList<String>();
		Collections.addAll(arraylist3,"foo", "bar", "baz");

		System.out.println(arraylist1);
		System.out.println(arraylist2);
		System.out.println(arraylist3);
		
		
		List<String> linkedlist1 = new LinkedList<String>() {
			{
				add("A");
				add("B");
				add("C");
			}
		};
		
		List<String> linkedlist2 = new LinkedList<>(Arrays.asList("foo", "bar"));
		arraylist2.add("baz");
		
		List<String> linkedlist3 = new LinkedList<String>();
		Collections.addAll(linkedlist3,"foo", "bar", "baz");
		
		System.out.println(linkedlist1);
		System.out.println(linkedlist2);
		System.out.println(linkedlist3);

	

		// Java 9 or later
		// List<String> arraylist3 = new ArrayList<>(List.of("A", "B", "C", "D"));
		// System.out.println(arraylist3);

		String[] array = { "A", "B", "C", "D" };
		List<String> list3 = new LinkedList<>(Arrays.asList(array));
		System.out.println(list3);

		ArrayList<String> places = createArrayList("São Paulo", "Rio de Janeiro", "Brasília");
		System.out.println(places);

	}

	@SafeVarargs
	public static <T> ArrayList<T> createArrayList(T...elements) {
		ArrayList<T> list = new ArrayList<T>();
		for (T element : elements) {
			list.add(element);
		}
		return list;
	}
}
