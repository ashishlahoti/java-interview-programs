package com.example.core.datastructure;

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

	Node<Integer> first, last;
	
	Node<Integer> enque(int i){
		Node<Integer> n = new Node<>(i);
		if(last!=null){
			n.setNext(last);
		}
		last = n;
		if(first==null){
			first = n;
		}
		return n;
	}
	
	int deque(){
		Node<Integer> n = last;
		Node<Integer> previous = null;
		while(n.getNext()!=null){
			previous = n;
			n = n.getNext();
		}
		n = previous.getNext();
		previous.setNext(null);
		return n.getData();
	}
	
	void print(){
		Node n = last;
		System.out.print("Last ");
		while(n != null){
			System.out.print(n.getData() + ", ");
			n = n.getNext();
		}
		System.out.println("First ");
		
	}
}
