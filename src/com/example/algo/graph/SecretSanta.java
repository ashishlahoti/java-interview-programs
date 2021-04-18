package com.example.algo.graph;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SecretSanta {

	public static void main(String[] args) {
		List<String> list = Arrays.asList("Adam", "Bill", "Charlie", "David", "Eva");
		// Collections.shuffle(list);
		shuffle(list);
		System.out.println(list);
		// System.out.println(getPairs(list));
	}

	public static Map<String, String> getPairs(List<String> list) {
		Map<String, String> map = new LinkedHashMap<>();
		if (list.size() <= 1) {
			return map;
		}
		for (int i = 0; i < list.size() - 1; i++) {
			map.put(list.get(i), list.get(i + 1));
		}
		map.put(list.get(list.size() - 1), list.get(0));
		return map;
	}

	public static void shuffle(List<String> list) {
		Random random = new Random();
		for (int i = 0; i < list.size(); i++) {
			int randomIndex = random.nextInt(list.size());
			String temp = list.get(randomIndex);
			list.set(randomIndex, list.get(i));
			list.set(i, temp);
		}
	}
}
