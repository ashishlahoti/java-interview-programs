package com.example.collection;

import java.util.HashSet;
import java.util.Random;

public class HashSetTest {

	
	public static void main(String[] args){
		HashSet<Element> set = new HashSet<>();
		for(int i= 0; i < 10; i++){
			Element el = new Element();
			set.add(el);
		}
		System.out.println(set.size());
	}
}

class Element {
	private String name;
	
	@Override
	public int hashCode() {
		Random rn = new Random();
		int randomNum = rn.nextInt(2);
		System.out.println("HashCode: " +randomNum);
		return randomNum;
	}
	
	@Override
	public boolean equals(Object obj) {
		return true;
	}
}
