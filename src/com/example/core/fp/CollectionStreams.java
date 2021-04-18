package com.example.core.fp;

import java.util.ArrayList;
import java.util.List;

public class CollectionStreams {

	public static void main(String[] args) {
		 List<Integer> list = new ArrayList<>();
		 list.stream().reduce((a,  b) -> a+b).get();
		 
	}
}
