package com.example.datastructure;

public class LinkedList<T> {

	public static void main(String[] args) {
		LinkedList<Integer> list = new LinkedList<>();
		list.add(2);
		list.add(4);
		list.add(6);
		list.add(1);
		list.add(5);
		list.add(10, 0);
		list.add(12, 1);
		list.add(19, 10);
		list.add(20);
		System.out.println(list);
		System.out.println(list.findMiddleElementIfSizeUnknown());
		list.reverse(list.head, null);
		System.out.println(list);
		System.out.println("0 : " + list.get(0));
		System.out.println("-2 : " + list.get(-2));
		System.out.println("12 : " + list.get(12));
		ListNode<Integer> node5 = list.get(8);
		System.out.println("deleting node: " + node5);
		list.delete(node5);
		System.out.println(list);
		int index = 7;
		System.out.println("deleting node at index: " + index);
		list.delete(index);
		System.out.println(list);

		list.rotateRight(list.head, 7);
		System.out.println(list);
	}

	ListNode<T> head = null;
	int size = 0;

	public ListNode<T> get(int index) {
		ListNode<T> node = head;
		for (int i = 0; i < index && node != null; i++) {
			node = node.next;
		}
		return node;
	}

	public void reverse(ListNode<T> current, ListNode<T> previous) {
		head = current;
		if (current.next != null) {
			reverse(current.next, current);
		}
		current.next = previous;
	}

	public void add(T t) {
		ListNode<T> newNode = new ListNode<>(t);
		if (head == null) {
			head = newNode;
		} else {
			ListNode<T> temp = head;
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = newNode;
		}
		size++;
	}

	public void delete(ListNode<T> node) {
		if (node != null) {
			ListNode<T> next = node.next;
			if (next != null) {
				ListNode<T> nextnext = node.next.next;
				node.val = next.val;
				node.next = nextnext;
				size--;
			} else {
				delete(size - 1);
			}
		}
	}

	public void delete(int index) {
		if (index >= 0 && index < size) {
			ListNode<T> temp = head;
			for (int i = 1; i < index; i++) {
				temp = temp.next;
			}
			ListNode<T> nodeToBeDeleted = temp.next;
			temp.next = (nodeToBeDeleted.next);
			size--;
		}
	}

	public void add(T t, int index) {
		ListNode<T> newNode = new ListNode<>(t);
		if (head == null) {
			head = newNode;
		} else if (index <= 0) {
			ListNode<T> temp = head;
			head = newNode;
			head.next = temp;
		} else if (index > 0) {
			ListNode<T> temp = head;
			for (int i = 1; i < index; i++) {
				if (temp.next != null) {
					temp = temp.next;
				} else {
					break;
				}
			}
			newNode.next = (temp.next);
			temp.next = (newNode);
		}
		size++;
	}

	public T findMiddleElementIfSizeUnknown() {
		ListNode<T> slow = head;
		ListNode<T> fast = head;
		while (fast != null && fast.next != null) {
			fast = fast.next.next;
			slow = slow.next;
		}
		return slow.val;
	}

	public T findMiddleElementIfSizeKnown() {
		return get(size / 2).val;
	}

	public ListNode<T> rotateRight(ListNode<T> head, int k) {
		System.out.println("Rotate right by " + k);
		if (head == null || k <= 0) {
			return head;
		}
		ListNode<T> startNode = head;
		ListNode<T> endNode = head;
		int counter = 0;
		int size = 1;
		while (endNode.next != null) {
			endNode = endNode.next;
			size++;
		}
		endNode = head;
		k = k % size;
		while (endNode.next != null) {
			counter += 1;
			if (counter > k) {
				startNode = startNode.next;
			}
			endNode = endNode.next;
		}
		endNode.next = head;
		this.head = startNode.next;
		startNode.next = null;
		return head;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("List of size " + size + ": ");
		ListNode<T> temp = head;
		if (temp != null) {
			builder.append(temp.val).append(", ");
			while (temp.next != null) {
				temp = temp.next;
				builder.append(temp.val).append(", ");
			}
		}
		return builder.toString();
	}

	public int size() {
		ListNode<T> node = head;
		int size = 1;
		while (node.next != null) {
			node = node.next;
			size++;
		}
		return size;
	}

}