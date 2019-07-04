package com.help4j.core.fp;

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
		
		
		/****
		 * STREAM APIs
		 */
		//Print number 1 to 10 using streams
		IntStream.range(1, 11).forEach((i) -> System.out.print(i + ","));
		
		//Stream of Array - sum array elements
		int[] intArray = new int[]{1, 5, 6, 8, 10, 11, 45, 68};
		int sum = Arrays.stream(intArray).sum();
		System.out.println("sum: " + sum);
		
		//Stream of Collection - print String array using streams
		a1.stream().forEach((s) -> System.out.print(s + ","));
		
		//filter(), map(), sorted() - intermediate operations
		//collect() - terminal operations
		List<String> fList = a1.stream().filter((s) -> s.startsWith("f")).map((s) -> s.toUpperCase()).sorted().collect(Collectors.toList());
		System.out.println();
		System.out.println(fList);
		
		//match() - terminal operations
		a1.stream().anyMatch((s) -> s.startsWith("e")); //returns true
		a1.stream().map((s) -> s.toUpperCase()).anyMatch((s) -> s.startsWith("E")); //returns true
		Stream.of("even", "elephant").allMatch((s) -> s.contains("e")); //returns true
		Stream.of("even", "elephant").noneMatch((s) -> s.contains("z")); //returns true
		
		//reduce() - terminal operations
		System.out.println(IntStream.range(1, 11).reduce((a, s) -> s += a).getAsInt());
		
		System.out.println(Stream.of("equity", "stocks", "gold", "foreign exchange","fixed income", "future").filter((s) -> s.startsWith("f")).findFirst().get());
		
		fp.execute();
		fp.print("ashish");
		
		
		
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
