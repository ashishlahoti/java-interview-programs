package com.example.datastructure;

public class Stack {
	
	public static void main(String[] args){
		Stack stack = new Stack();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.print();
		stack.pop();
		stack.print();
		stack.reverse();
		stack.print();
	}

	ListNode<Integer> top;
	
	ListNode<Integer> next = null;
	
	ListNode<Integer> push(int i){
		ListNode<Integer> n = new ListNode<>(i);
		n.next = (top);
		top = n;
		return n;
	}
	
	
	
	int pop(){
		ListNode<Integer> n = top;
		top = top.next;
		return n.val;
	}
	
	void print(){
		System.out.println("TOP");
		if(top!=null){
			ListNode<Integer> n = top;
			while(n != null){
				System.out.println(n.val);
				n = n.next;
			}
		}
	}
	

	void reverse(){
		reverse(top, top.next);
	}
	
	void reverse(ListNode<Integer> current, ListNode<Integer> nextNode) {
		top = nextNode;
		
		current.next = (next);
		next = nextNode;
		
		if(nextNode.next != null)
			reverse(nextNode, nextNode.next);
		nextNode.next = (current);
	}
	
	boolean isEmpty(){
		return top == null;
	}
}
