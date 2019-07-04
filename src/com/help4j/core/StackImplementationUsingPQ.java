package com.help4j.core;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;


public class StackImplementationUsingPQ {

	private PriorityQueue<Element> pq = new PriorityQueue<Element>();
	private int counter = 0;
	
	public void push(String s){
		Element e = new Element(s, counter++);
		pq.add(e);
	}
	
	public String pop(){
		Element e = null;
		try{
			e = pq.remove();
			counter--;
		}catch(NoSuchElementException ex){
			return "'Stack is Empty'";
		}
		return e.toString();
	}
	
	public static void main(String[] args){
		StackImplementationUsingPQ stack = new StackImplementationUsingPQ();
		stack.push("Banana");
		stack.push("Mango");
		stack.push("Apple");
		System.out.println(stack.pop());
		stack.push("Orange");
		stack.push("Pinapple");
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
	}
	
}
class Element implements Comparable<Element>{
	
	private Integer priority;
	private String value;
	
	Element(String value, Integer priority){
		this.value = value;
		this.priority = priority;
	}
	
	
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(Element o) {
		return o.getPriority().compareTo(this.getPriority());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("value : ");
		sb.append(getValue());
		sb.append(", ");
		sb.append("priority : ");
		sb.append(getPriority());
		return sb.toString();
	}
}


