package com.example.datastructure;

import java.util.NoSuchElementException;

public class Dequeue {

	ListNode<Integer> first;
	ListNode<Integer> last;
	int size;

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return size;
	}

	public Dequeue() {
		first = null;
		last = null;
		size = 0;
	}

	public void addFirst(int value) {
		ListNode<Integer> node = new ListNode<>(value);
		if (first == null) {
			first = node;
			last = node;
		} else {
			node.next = first;
			first = node;
		}
		size++;
	}

	public void addLast(int value) {
		ListNode<Integer> node = new ListNode<>(value);
		if (last == null) {
			first = node;
			last = node;
		} else {
			last.next = node;
			last = node;
		}
		size++;
	}

	public void add(int value) {
		addLast(value);
	}

	public int removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException("Empty Queue");
		}
		int value = first.val;
		first = first.next;
		size--;
		return value;
	}

	public int removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException("Empty Queue");
		}
		ListNode<Integer> prev = null;
		ListNode<Integer> temp = first;
		while (temp != last) {
			prev = temp;
			temp = temp.next;
		}
		int value = last.val;
		last = prev;
		size--;
		return value;
	}

	public void remove() {
		removeFirst();
	}

	public void print() {
		ListNode<Integer> temp = first;
		System.out.print("List [ ");
		while (temp != last) {
			System.out.print(temp.val + " ");
			temp = temp.next;
		}
		System.out.print(last.val + " ");
		System.out.println("]");
	}

	public static void main(String[] args) {
		Dequeue queue = new Dequeue();
		queue.add(5);
		queue.add(6);
		queue.addFirst(4);
		queue.addFirst(3);
		queue.print();
		queue.remove();
		queue.removeLast();
		queue.print();
	}
}
