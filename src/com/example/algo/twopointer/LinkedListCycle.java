package com.example.algo.twopointer;

public class LinkedListCycle {

	public static void main(String[] args) {
		ListNode node = new ListNode(3);
		node.next = new ListNode(2);
		node.next.next = new ListNode(0);
		node.next.next.next = new ListNode(-4);
		System.out.println(hasCycle(node));
		node.next.next.next = node;
		System.out.println(hasCycle(node));
	}
	
	public static boolean hasCycle(ListNode head) {
		if (head == null) {
			return false;
		}

		ListNode slow = head;
		ListNode fast = head.next;

		while (slow != fast) {
			if (fast == null || fast.next == null) {
				return false;
			}
			slow = slow.next;
			fast = fast.next.next;
		}

		return true;
	}
}

class ListNode {
	int val;
	ListNode next;

	ListNode(int x) {
		val = x;
		next = null;
	}
}