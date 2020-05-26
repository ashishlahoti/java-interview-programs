package com.example.designpattern;

import java.util.ArrayList;
import java.util.List;

public class ObserverPattern {

	public static void main(String[] args) {
		User lisa = new User("Lisa");
		User mona = new User("Mona");
		User simona = new User("Simona");
		Dress dress = new Dress();
		dress.register(lisa);
		dress.register(mona);
		dress.register(simona);
		dress.setInStock(true);
		dress.unregister(lisa);
		dress.setInStock(true);

	}
}

interface Observer {

	public void update();
}

abstract class Subject {

	List<Observer> observers = new ArrayList<Observer>();

	public void notifyAllObservers() {
		for (Observer observer : observers) {
			observer.update();
		}
	}

	public void register(Observer o) {
		observers.add(o);
	}

	public void unregister(Observer o) {
		observers.remove(o);
	}
}

class User implements Observer {

	private String name;

	public User(String name) {
		this.name = name;
	}

	@Override
	public void update() {
		System.out.println(this.name + " notified");
	}

	@Override
	public String toString() {
		return this.name;
	}

}

class Dress extends Subject {

	private boolean isInStock = true;

	public boolean isInStock() {
		return isInStock;
	}

	public void setInStock(boolean isInStock) {
		this.isInStock = isInStock;
		notifyAllObservers();
	}

}
