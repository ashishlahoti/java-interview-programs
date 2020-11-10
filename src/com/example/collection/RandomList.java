package com.example.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomList {

	public static void main(String[] args) {
		Random random = new Random();

		List<String> users = Arrays.asList("Adam", "Bill", "Charlie", "David", "Electra", "Fuji", "Garry", "Harley");
		System.out.println(users);
		Collections.shuffle(users);
		System.out.println(users);

		int[] array1 = random.ints(10, 0, 50).toArray();
		int[] array2 = IntStream.range(0, 10).map(i -> random.nextInt(50)).toArray();

		System.out.println(Arrays.toString(array1));
		System.out.println(Arrays.toString(array2));
	}
}
