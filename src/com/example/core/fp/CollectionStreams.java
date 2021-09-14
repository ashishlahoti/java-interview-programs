package com.example.core.fp;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionStreams {

	public static void main(String[] args) {

		/** map vs flatMap */
		Map<String, List<String>> people = new HashMap<>();
		people.put("John", Arrays.asList("555-1123", "555-3389"));
		people.put("Mary", Arrays.asList("555-2243", "555-5264"));
		people.put("Steve", Arrays.asList("555-6654", "555-3242"));

		List<String> names = people.keySet().stream()
			.map(String::toUpperCase)
			.collect(Collectors.toList());
		System.out.print(names); //[STEVE, JOHN, MARY]

		List<String> phones = people.values().stream()
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		System.out.println(phones); //[555-6654, 555-3242, 555-1123, 555-3389, 555-2243, 555-5264]


		/** findFirst vs findAny */
		List<String> users = Arrays.asList("David", "Jack", "Duke", "Jill", "Dany", "Julia", "Peter");

		Optional<String> findFirst = users.parallelStream().filter(s -> s.startsWith("D")).findFirst();
		Optional<String> findAny = users.parallelStream().filter(s -> s.startsWith("D")).findAny();

		System.out.println(findFirst.get()); //Always print David
		System.out.println(findAny.get()); //Print David or Duke or Dany :behavior of this operation is explicitly nondeterministic


		/** Intermediate vs Terminal Operations */

		System.out.println("Stream without terminal operation");

		Arrays.stream(new int[] { 1, 2, 3 })
			.map(i -> {
				int n = i*2;
				System.out.println("doubling " + i + "=" + n);
				return n;
			});

		System.out.println("Stream with terminal operation");
		Arrays.stream(new int[] { 1, 2, 3 })
			.map(i -> {
				int n = i*2;
				System.out.println("doubling " + i + "=" + n);
				return n;
			}).sum();
	}
}
