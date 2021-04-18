package com.example.datastructure;

public class LinkedList<T> {

	public static void main(String[] args) {
		LinkedList<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		System.out.println(list);
		list.reverse();
		System.out.println(list);
	}

	ListNode<T> head;

	public LinkedList() {
		head = null;
	}

	public ListNode<T> add(T val) {
		ListNode<T> newNode = new ListNode<T>(val);
		if (head == null) {
			head = newNode;
		} else {
			ListNode<T> temp = head;
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = newNode;
		}
		return head;
	}

	// 1 2 3 4
	// prev current next
	public ListNode<T> reverse() {
		ListNode<T> prev = null;
		ListNode<T> current = head;
		ListNode<T> next;
		while (current != null) {
			next = current.next;
			current.next = prev;
			prev = current;
			current = next;
		}
		head = prev;
		return head;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("List ");
		ListNode<T> temp = head;
		while (temp != null) {
			builder.append(temp.val + " ");
			temp = temp.next;
		}
		return builder.toString();
	}

}

class ListNode<T> {

	T val;

	ListNode<T> next;

	ListNode(T val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return val.toString();
	}
}