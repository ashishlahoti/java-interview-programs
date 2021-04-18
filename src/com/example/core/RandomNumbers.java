package com.example.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomNumbers {

	public static void main(String[] args) {
		// Creating an object of Random class
		Random random = new Random();
		// Calling the nextInt() method
		System.out.println("A random int: " + random.nextInt());
		// Calling the overloaded nextInt() method
		System.out.println("A random int from 0 to 49: " + random.nextInt(50));
		// Calling the nextDouble() method
		System.out.println("A random double: " + random.nextDouble());
		// Calling the nextFloat() method
		System.out.println("A random float: " + random.nextFloat());
		// Calling the nextLong() method
		System.out.println("A random long: " + random.nextLong());

		System.out.println("Array(5) of numbers between 0 to 99: " + Arrays.toString(generateRandomArray(5, 0, 100)));
		System.out.println("List(5) of numbers between 0 to 99: " + generateRandomList(5, 0, 100));

		// generate 5 random number between 0 to 99
		int[] randomIntsArray1 = IntStream.generate(() -> new Random().nextInt(100)).limit(5).toArray();
		System.out.println("Array(5) of numbers between 0 to 99: " + Arrays.toString(randomIntsArray1));
		// generate 5 random number between 100 to 199
		int[] randomIntsArray2 = IntStream.generate(() -> new Random().nextInt(100) + 100).limit(5).toArray();
		System.out.println("Array(5) of numbers between 100 to 199: " + Arrays.toString(randomIntsArray2));
		
		
		String[] users = new String[] { "Adam", "Bill", "Charlie", "David", "Eva", "Fang", "George", "Harry", "Ivy", "Jack" };
		System.out.println("Get Random User: " + users[random.nextInt(users.length)]);
		
		
		List<String> userList = Arrays.asList("Adam", "Bill", "Charlie", "David", "Eva", "Fang", "George", "Harry", "Ivy", "Jack");
		Collections.shuffle(userList);
		System.out.println(userList); 
		Collections.shuffle(userList);
		System.out.println(userList); 
		
		Collections.shuffle(userList);
		List<String> fiveUsers = userList.subList(0, 5);
		System.out.println(fiveUsers);
	}

	public static int[] generateRandomArray(int size, int minValueInclusive, int maxValueExclusive) {
		int[] array = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			array[i] = minValueInclusive + random.nextInt(maxValueExclusive);
		}
		return array;
	}

	public static int[] generateRandomArray1(int size, int minValueInclusive, int maxValueExclusive) {
		return IntStream.generate(() -> new Random().nextInt(maxValueExclusive) + minValueInclusive).limit(size)
				.toArray();
	}

	public static List<Integer> generateRandomList(int size, int minValueInclusive, int maxValueExclusive) {
		return IntStream.generate(() -> new Random().nextInt(maxValueExclusive) + minValueInclusive).limit(size).boxed().collect(Collectors.toList());

	}
}
