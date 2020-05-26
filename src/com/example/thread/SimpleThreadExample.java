package com.example.thread;

public class SimpleThreadExample {

	public static void main(String[] args){
		//startThreads(10);
		startCounterThreads(20);
		
	}
	
	public static void startThreads(int num) {
		for(int i=1; i<=num; i++){
			int index = i;
			new Thread(() -> System.out.println("Thread_" + index)).start();
		}
	}
	
	public static void startCounterThreads(int num) {
		Counter counter = new Counter();
		for(int i=1; i<=num; i++){
			CounterThread counterThread = new CounterThread(counter);
			counterThread.setName("Thread_" + i);
			counterThread.start();
		}
	}
}

class CounterThread extends Thread{
	Counter counter;
	CounterThread(Counter counter){
		this.counter = counter;
	}
	public void run(){
		counter.add(1);
		System.out.println(Thread.currentThread().getName() + " count: " + counter.count);
	}
}
class Counter{

	long count = 0;
	public void add(long value){
        this.count = this.count + value;
    }
}
