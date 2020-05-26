package com.example.thread;

public class PrintNumbersUsingTwoThreads {
	public static void main(String[] args){
		Print counter = new Print(0);
		Thread threadAObj = new Thread(new PrintingThread(counter, "Odd", false));
		threadAObj.setName("Thread Odd");
		Thread threadBObj = new Thread(new PrintingThread(counter, "Even", true));
		threadBObj.setName("Thread Even");
		threadAObj.start();
		threadBObj.start();
	}
}

class PrintingThread implements Runnable {
	Print counter;
	boolean isEven;
	String name;
	PrintingThread(Print counter, String name, boolean isEven){
		this.counter = counter;
		this.name= name;
		this.isEven = isEven;
	}
	@Override
	public void run() {
		synchronized (counter) {
			while(((isEven && counter.number%2==1) || (!isEven && counter.number%2==0)) && counter.number < 10){
				counter.increment();
				System.out.println(Thread.currentThread().getName() + " = " + counter);
				counter.notify();
				try {
					counter.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class Print {
	int number;
	Print(int number){
		this.number = number;
	}
	void increment(){
		this.number = this.number+1;
	}
	@Override
	public String toString() {
		return number+"";
	}
}