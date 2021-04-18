package com.example.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapExamples {

	public static void main(String[] args) {
		HashMap<String, List<Integer>> map = new HashMap<>();
		map.putIfAbsent("Ashish", new ArrayList<>());		
		map.compute("Ashish", (k, v) -> {v.add(1); return v;});
		System.out.println(map);
	}
}
