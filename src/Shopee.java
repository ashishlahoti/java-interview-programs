
//Write a function to reverse a singly linked list (can't modify the value inside each node)

public class Shopee {

	public static void main(String[] args) {
		LinkedList list = new LinkedList();
		list.add(1);
		list.add(2);
		list.add(3);
		list.print();
		list.reverse();
		list.print();
	}
}

class Node {
	int val;
	Node next;

	public Node(int val) {
		this.val = val;
	}
}

class LinkedList {

	Node head;

	public LinkedList() {
		this.head = null;
	}

	public void add(int val) {
		Node node = new Node(val);
		if (head == null) {
			head = node;
		} else {
			Node temp = head;
			while (temp.next != null) {
				temp = temp.next;
			}
			temp.next = node;
		}
	}

	public void print() {
		Node temp = this.head;
		while (temp != null) {
			System.out.println(temp.val);
			temp = temp.next;
		}
	}

	// A -> B -> C -> NULL
	// prev. current. next
	public void reverse() {
		if (this.head == null) {
			return;
		}
		reverse(this.head, null);
	}

	public void reverse(Node current, Node previous) {
		this.head = current;
		if (current.next != null) {
			reverse(current.next, current);
		}
		current.next = previous;
	}
}