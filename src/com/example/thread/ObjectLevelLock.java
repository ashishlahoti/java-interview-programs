package com.example.thread;

public class ObjectLevelLock {
	public static void main(String[] args) {
		Print counter = new Print(0);
		Print counter1 = new Print(0);
		Thread threadAObj = new Thread(new PrintingThread1(counter1));
		threadAObj.setName("Thread Odd");
		Thread threadBObj = new Thread(new PrintingThread1(counter));
		threadBObj.setName("Thread Even");
		threadAObj.start();
		threadBObj.start();
	}
	
}

class PrintingThread1 implements Runnable {
	Print counter;
	PrintingThread1(Print counter){
		this.counter = counter;
	}
	@Override
	public void run() {
		synchronized (counter) {
			for (int i = 0; i <10; i++) {
				System.out.println(Thread.currentThread().getName() + " = " + i);
				try {
					counter.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}