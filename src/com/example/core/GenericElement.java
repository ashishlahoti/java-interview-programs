package com.example.core;

public class GenericElement<T> {

	private T t;

	public void set(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}

	public static void main(String[] args) {
		GenericElement<Integer> num = new GenericElement<>();
		num.set(2);
		System.out.println(num.get());

		GenericElement<String> str = new GenericElement<>();
		str.set("generics");
		System.out.println(str.get());
	}
}
