package com.example.core.fp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class DoubleColonUsage {

	public static void main(String[] args) {
		
		List<Integer> numbers = Arrays.asList(1,2,3,4,5);
		numbers.forEach(number->System.out.println(number));
		
		
		//numbers.forEach(System.out::println);
		
		List<String> languages = Arrays.asList("java", "javascript", "css", "html");
		// anonymous class
		languages.forEach(new Consumer<String>() {       
		    @Override
		    public void accept(String str) {
		        System.out.println(str);
		    }
		});
		// lambda
		//languages.forEach(str -> System.out.println(str));
		// method references
		//languages.forEach(System.out::println); 
		
		// lambda expression
		//languages.stream().map(str -> str.toUpperCase()).forEach(str -> System.out.println(str));

		// method reference
		//languages.stream().map(String::toUpperCase).forEach(System.out::println);;    
		
		numbers.stream().map(number -> new Calculator().square(number)).forEach(number -> System.out.println(number));
		
		//numbers.stream().map(Calculator::square).forEach(System.out::println);
		
		List<String> str_numbers = Arrays.asList("10", "20", "30");
		List<String> str_doubles = Arrays.asList("10.31", "20.96", "30.14");

		// Lambda expression
		//str_numbers.stream().map(n -> Integer.parseInt(n)).forEach(n -> System.out.println(n));
		//str_doubles.stream().map(d -> Double.parseDouble(d)).forEach(d -> System.out.println(d));

		// Method Reference
		//str_numbers.stream().map(Integer::parseInt).forEach(System.out::println);
		//str_doubles.stream().map(Double::parseDouble).forEach(System.out::println);
		
		List<String> years = Arrays.asList("2019", "2020", "2021");

        
		// Lambda expression
		//years.stream().map(year -> Integer.parseInt(year)).forEach(year -> System.out.println(year));

		// Method Reference
		//years.stream().map(Integer::parseInt).forEach(System.out::println);
		 
		List<Tutorial> tutorials = Arrays.asList(new Tutorial[] {
				new Tutorial("Streams in Java 8", 30, 4.2),
				new Tutorial("What's new in Java 11", 25, 4.8),
				new Tutorial("Core Java Concepts", 45, 3.5)});
		
		tutorials.forEach(tutorial -> System.out.println(tutorial));
		//tutorials.forEach(System.out::println);

		//static method
		//tutorials.stream().map(tutorial -> Tutorial.toUpperCase(tutorial)).forEach(s -> System.out.println(s));
		//tutorials.stream().map(Tutorial::toUpperCase).forEach(System.out::println); 
		
		//instance method
		//tutorials.stream().map(tutorial -> tutorial.getName()).map(name -> name.toUpperCase()).forEach(s -> System.out.println(s));
		//tutorials.stream().map(Tutorial::getName).map(String::toUpperCase).forEach(System.out::println);
		//tutorials.stream().sorted((tutorial1, tutorial2) -> Tutorial.compareByRating(tutorial1, tutorial2)).forEach(tutorial -> System.out.println(tutorial));
		//tutorials.stream().sorted((tutorial1, tutorial2) -> Tutorial.compareByDuration(tutorial1, tutorial2)).forEach(tutorial -> System.out.println(tutorial));
		//tutorials.stream().sorted(Tutorial::compareByRating).forEach(System.out::println);
		//tutorials.stream().sorted(Tutorial::compareByDuration).forEach(System.out::println);
		
		 //Supplier<Map> obj1 = () -> new HashMap();
		 
		 
		//Supplier<int[]> arrayMaker1 = () -> new int[10];
		//System.out.println(arrayMaker1.get().length);
		
		IntFunction<int[]> arrayMaker = int[]::new;
		int[] array = arrayMaker.apply(10);  // creates an int[10]
		
		// lambda
        Supplier<Map> obj11 = () -> new HashMap();   // default HashMap() constructor
        Map map1 = obj11.get();

        // method reference
        Supplier<Map> obj2 = HashMap::new;
        Map map2 = obj2.get();
        
        Function<String, Tutorial> tutorial1 = Tutorial::new;
        Tutorial t1 = tutorial1.apply("Tutorial 1");
        
        BiFunction<String, Integer, Tutorial> tutorial2 = Tutorial::new; 
        Tutorial t2 = tutorial2.apply("Tutorial 2", 25);
       
        TriFunction<String, Integer, Double, Tutorial> tutorial3 = Tutorial::new;
        Tutorial t3 = tutorial3.apply("Tutorial 3", 30, 4.9);
        
        //Arrays.asList(t1, t2, t3).forEach(System.out::println);
        
        Function<String, Integer> parseInt = Integer::parseInt;
        System.out.println(parseInt.apply("2019"));
        
        Function<Double, Double> square = new Calculator()::square;
        BiFunction<Double, Double, Double> multiply = new Calculator()::multiply;
        System.out.println(square.apply(3.0));
        System.out.println(multiply.apply(2.5, 5.0));
        
        Function<String, String> toUpperCase = String::toUpperCase;

        // Execute function
        System.out.println(toUpperCase.apply("java"));
        
        Consumer<String> println = System.out::println;
        println.accept("Learn Colon Operator");
        
		List<AdvanceTutorial> adv_tutorials = Arrays.asList(new AdvanceTutorial[] {
				new AdvanceTutorial("Streams in Java 8", 30, 4.2),
				new AdvanceTutorial("What's new in Java 11", 25, 4.8)});
	}
}

class AdvanceCalculator extends Calculator {
	
	public double squareAndAdd(double num1, double num2) {
    	Function<Double, Double> square = super::square; 	
    	return square.apply(num1)* square.apply(num2);
    }
	
	public double squareAndMultiply(double num1, double num2) {
    	Function<Double, Double> square = super::square; 
    	BiFunction<Double, Double, Double> multiply = super::multiply;
    	return multiply.apply(square.apply(num1), square.apply(num2));
    }
}

class Calculator {
	
    public double square(double num){
       return Math.pow(num, 2);
    }
    
    public double multiply(double num1, double num2) {
    	return num1*num2;
    }
}

@FunctionalInterface
interface TriFunction<A, B, C, R> { 
    R apply(A a, B b, C c); 
    default <V> TriFunction<A, B, C, V> andThen( Function<? super R, ? extends V> after) { 
        Objects.requireNonNull(after); 
        return (A a, B b, C c) -> after.apply(apply(a, b, c)); 
    } 
}

class AdvanceTutorial extends Tutorial {
	
	private Boolean advance;
	
	AdvanceTutorial(String name, Integer duration, Double rating){
		super(name, duration, rating);
		this.advance = true;
	}

	public Boolean getAdvance() {
		return advance;
	}

	public void setAdvance(Boolean advance) {
		this.advance = advance;
	}
}

class Tutorial {
	private String name;
    private Integer duration;
    private Double rating;
    
    public Tutorial(String name) {
		this.name = name;
		this.duration = 0;
		this.rating = 0.0;
	}
    
    public Tutorial(String name, Integer duration) {
		this.name = name;
		this.duration = duration;
		this.rating = 0.0;
	}
    
	public Tutorial(String name, Integer duration, Double rating) {
		this.name = name;
		this.duration = duration;
		this.rating = rating;
	}
    
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
   
	@Override
	public String toString() {
		return "Tutorial[ " + name + "\t- " + duration + "min, rating=" + rating + " ]";
	}
	
	public static int compareByRating(Tutorial t1, Tutorial t2) {
        return t1.getRating().compareTo(t2.getRating());
    }
	public static int compareByDuration(Tutorial t1, Tutorial t2) {
        return t1.getDuration().compareTo(t2.getDuration());
    }
}