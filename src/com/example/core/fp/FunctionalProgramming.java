package com.example.core.fp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class FunctionalProgramming implements Java8Interface1 {

	public static void main(String[] args){
		
		FunctionalProgramming fp = new FunctionalProgramming();
		
		//Java 7 way of writing
		Thread t1 = new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("Thread started in Java7");
			}
			
		});
		t1.start();
		
		//Java 8 way of writing
		Thread t2 = new Thread(() -> System.out.println("Thread started in Java8"));
		t2.start();
		
		
		//Java 7 way of writing
		List<String> a1 = Arrays.asList("equity", "stocks", "gold", "foreign exchange","fixed income", "future");
		a1.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		System.out.println(a1);
		
		//Java 8 way of writing
		a1.sort((o1, o2) -> o2.compareTo(o1));
		System.out.println(a1);
		
		
		int[] intArray = new int[]{1, 3, 5, 7, 9, 3, 5, 99};
		int sum = Arrays.stream(intArray)
		                .sum();

		long count = Arrays.stream(intArray)
		                .count();

		int min = Arrays.stream(intArray)
		                .min()
		                .getAsInt();

		int max = Arrays.stream(intArray)
		                .max()
		                .getAsInt();

		System.out.println("sum: " + sum);
		System.out.println("count: " + count);
		System.out.println("min: " + min);
		System.out.println("max: " + max);
		System.out.println("distinct:");

		Arrays.stream(intArray)
		      .distinct()
		      .forEach(System.out::println);
		
	}

	public void print(String s){
		System.out.println("Class: " + s);
	}
	public static void test(boolean result){
		System.out.println(result);
	}

	@Override
	public void execute() {
		String s = "";
		if(Java8Interface1.isNull(s)){
			System.out.println("String is null");
		}
	}
	
	public interface MyInterface{
		public int add(int a, int b);
	}
}
