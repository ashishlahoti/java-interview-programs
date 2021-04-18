package com.example.collection;

import java.util.PriorityQueue;

public class ProrityQueueTest {

	public static void main(String[] args) {
		PriorityQueue<Integer> queue = new PriorityQueue<>((a,b) -> b-a);
		
		queue.add(5);
		queue.add(1);
		queue.add(10);
		queue.add(11);
		queue.add(6);
		
		System.out.println(queue.remove());
		System.out.println(queue.remove());
		System.out.println(queue.remove());
		System.out.println(queue.remove());
	}
}
