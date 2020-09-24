package com.example.datastructure;

public class ListNode<T> {
	
	T val;	
	
	ListNode<T> next;

	ListNode(T val){
		this.val = val;
	}

	@Override
	public String toString() {
		return val.toString();
	}
}
