package com.example.core.datastructure;

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
		Node<Integer> node5 = list.get(8);
		System.out.println("deleting node: " + node5);
		list.delete(node5);
		System.out.println(list);
		int index = 7;
		System.out.println("deleting node at index: " + index);
		list.delete(index);
		System.out.println(list);

	}

	Node<T> head = null;
	int size = 0;

	public Node<T> get(int index) {
		Node<T> node = head;
		for (int i = 0; i < index && node != null; i++) {
			node = node.getNext();
		}
		return node;
	}

	public void reverse(Node<T> current, Node<T> previous) {
		head = current;
		if (current.getNext() != null) {
			reverse(current.getNext(), current);
		}
		current.setNext(previous);
	}

	public void add(T t) {
		Node<T> newNode = new Node<>(t);
		if (head == null) {
			head = newNode;
		} else {
			Node<T> temp = head;
			while (temp.getNext() != null) {
				temp = temp.getNext();
			}
			temp.setNext(newNode);
		}
		size++;
	}

	public void delete(Node<T> node) {
		if (node != null) {
			Node<T> next = node.getNext();
			if (next != null) {
				Node<T> nextnext = node.getNext().getNext();
				node.setData(next.getData());
				node.setNext(nextnext);
				size--;
			} else {
				delete(size - 1);
			}
		}
	}

	public void delete(int index) {
		if (index >= 0 && index < size) {
			Node<T> temp = head;
			for (int i = 1; i < index; i++) {
				temp = temp.getNext();
			}
			Node<T> nodeToBeDeleted = temp.getNext();
			temp.setNext(nodeToBeDeleted.getNext());
			size--;
		}
	}

	public void add(T t, int index) {
		Node<T> newNode = new Node<>(t);
		if (head == null) {
			head = newNode;
		} else if (index <= 0) {
			Node<T> temp = head;
			head = newNode;
			head.setNext(temp);
		} else if (index > 0) {
			Node<T> temp = head;
			for (int i = 1; i < index; i++) {
				if (temp.getNext() != null) {
					temp = temp.getNext();
				} else {
					break;
				}
			}
			newNode.setNext(temp.getNext());
			temp.setNext(newNode);
		}
		size++;
	}

	public T findMiddleElementIfSizeUnknown() {
		Node<T> slow = head;
		Node<T> fast = head;
		while (fast != null && fast.getNext() != null) {
			fast = fast.getNext().getNext();
			slow = slow.getNext();
		}
		return slow.getData();
	}

	public T findMiddleElementIfSizeKnown() {
		return get(size / 2).getData();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("List of size " + size + ": ");
		Node<T> temp = head;
		if (temp != null) {
			builder.append(temp.getData()).append(", ");
			while (temp.getNext() != null) {
				temp = temp.getNext();
				builder.append(temp.getData()).append(", ");
			}
		}
		return builder.toString();
	}

	public int size() {
		Node<T> node = head;
		int size = 1;
		while (node.getNext() != null) {
			node = node.getNext();
			size++;
		}
		return size;
	}

}

class LinkedList1<T> {

	Node<T> head = null;
	int size = 0;

	public Node<T> get(int index) {
		Node<T> node = head;
		// get by index
		return node;
	}

	public void add(T t) {
		// add element
		size++;
	}

	public void delete(int index) {
		// delete element
		size--;
	}

	public void findMiddleElement() {
		get((size - 1) / 2);
	}

}

class LinkedList2<T> {

	Node<T> head = null;

	public Node<T> get(int index) {
		Node<T> node = head;
		// get by index
		return node;
	}

	public int size() {
		Node<T> node = head;
		int size = 1;
		while (node.getNext() != null) {
			node = node.getNext();
			size++;
		}
		return size;
	}

	public void findMiddleElement() {
		get((size() - 1) / 2);
	}
}

class LinkedList3<T> {

	Node<T> head = null;

	public Node<T> findMiddleElement() {
		Node<T> slow = head;
		Node<T> fast = head;
		while (fast != null && fast.getNext() != null) {
			fast = fast.getNext().getNext();
			slow = slow.getNext();
		}
		return slow;
	}
}
