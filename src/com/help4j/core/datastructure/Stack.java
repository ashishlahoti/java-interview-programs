package com.help4j.core.datastructure;

public class Stack {
	
	public static void main(String[] args){
		Stack stack = new Stack();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.print();
		//stack.pop();
		//stack.print();
		stack.reverse();
		stack.print();
	}

	Node<Integer> top;
	
	Node<Integer> push(int i){
		Node<Integer> n = new Node<>(i);
		n.setNext(top);
		top = n;
		return n;
	}
	
	int pop(){
		Node<Integer> n = top;
		top = top.getNext();
		return n.getData();
	}
	
	void print(){
		System.out.println("TOP");
		if(top!=null){
			Node<Integer> n = top;
			while(n != null){
				System.out.println(n.getData());
				n = n.getNext();
			}
		}
	}

	void reverse(){
		reverse(top, null);
	}
	
	void reverse(Node<Integer> current, Node<Integer> previous){
		top = current;
		if(current.getNext() != null){
			reverse(current.getNext(), current);
		}
		current.setNext(previous);
	}
	
	boolean isEmpty(){
		return top == null;
	}
}
