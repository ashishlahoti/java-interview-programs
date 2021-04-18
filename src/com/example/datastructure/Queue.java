package com.example.datastructure;

// FIFO - First In First Out
public class Queue {
	
	public static void main(String[] args){
		Queue queue = new Queue();
		queue.enque(1);
		queue.enque(5);
		queue.enque(7);
		queue.enque(8);
		queue.print();
		queue.deque();
		queue.print();
		queue.enque(15);
		queue.print();
		queue.enque(20);
		queue.print();
		queue.deque();
		queue.print();
	}

	ListNode<Integer> first, last;
	
	ListNode<Integer> enque(int i){
		ListNode<Integer> n = new ListNode<>(i);
		if(last!=null){
			n.next = (last);
		}
		last = n;
		if(first==null){
			first = n;
		}
		return n;
	}
	
	int deque(){
		ListNode<Integer> n = last;
		ListNode<Integer> previous = null;
		while(n.next!=null){
			previous = n;
			n = n.next;
		}
		n = previous.next;
		previous.next = (null);
		return n.val;
	}
	
	void print(){
		ListNode<Integer> n = last;
		System.out.print("Last ");
		while(n != null){
			System.out.print(n.val + ", ");
			n = n.next;
		}
		System.out.println("First ");
		
	}
}
