package com.example.algo.graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

public class ZIterator {
	Queue<ListIterator<Integer>> queue;

	ZIterator(List<List<Integer>> lists) {
		this.queue = new LinkedList<ListIterator<Integer>>();
		for (List<Integer> list : lists) {
			queue.add(list.listIterator());
		}
	}

	public int next() {
		ListIterator<Integer> iterator = queue.remove();
		if (iterator.hasNext()) {
			queue.add(iterator);
			return iterator.next();
		} else {
			return next();
		}
	}
	
	public boolean hasNext() {
		ListIterator<Integer> iterator = queue.peek();
		if(iterator == null) {
			return false;
		}
		if (iterator.hasNext()) {
			return true;
		} else {
			queue.remove();
			return hasNext();
		}
	}
	
	public static void main(String[] args) {
		ZIterator iterator = new ZIterator(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(4, 5)));
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		
	}
}
