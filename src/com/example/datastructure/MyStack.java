package com.example.datastructure;

public class MyStack {
	
	private long[] stackArray;
	private int top;
	private int capacity;
	
	public MyStack(int capacity) {
		this.capacity = capacity;
		this.top = -1;
		this.stackArray = new long[capacity];
	}
	public static void main(String[] args) throws Exception{
		MyStack myStack = new MyStack(5);
		myStack.push(11);
		myStack.push(13);
		myStack.push(15);
		myStack.print();
		System.out.println("peek : " +  myStack.peek());
		System.out.println("peek : " +  myStack.peek());
		System.out.println("pop : " +  myStack.pop());
		System.out.println("peek : " +  myStack.peek());
		myStack.print();
	}
	
	public void push(long num) throws Exception{
		try{
			stackArray[++top] = num;
		}catch(ArrayIndexOutOfBoundsException ex){
			throw new Exception("Stack Overflow !!");
		}
	}
	
	public long pop(){
		return stackArray[top--];
	}
	
	public long peek(){
		return stackArray[top];
	}
	
	public boolean isEmpty(){
		return top < capacity;
	}
	
	public void print(){
		for(int i = 0; i <= top; i++){
			System.out.println(stackArray[i]);
		}
	}
}
