package com.example.datastructure;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LinkedListTest {

	@Test
	public void findMiddleElementOfLinkedList() {
		LinkedList<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		assertEquals(Integer.valueOf(3), list.findMiddleElementIfSizeUnknown());
		assertEquals(Integer.valueOf(3), list.findMiddleElementIfSizeKnown());
	}
	
	@Test
	public void traverseTogetSizeOfLinkedList() {
		LinkedList<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		assertEquals(1, list.size());
	}
}
