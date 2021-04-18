package com.example.core.oops;

public class Overiding {
	public static void main(String[] args) {
		Animal animal = new Animal();
		animal.makeSound();
		// prints "Don't Know"

		Animal dog = new Dog();
		dog.makeSound();
		// prints "Bark"

		Animal cat = new Cat();
		cat.makeSound();
		// prints "Meow"
	}
}

class Animal {
	public void makeSound() {
		System.out.println("Don't Know");
	}
}

class Dog extends Animal {
	public void makeSound() {
		System.out.println("Bark");
	}
}

class Cat extends Animal {
	public void makeSound() {
		System.out.println("Meow");
	}
}
