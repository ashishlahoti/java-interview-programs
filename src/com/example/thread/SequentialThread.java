package com.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SequentialThread {

	public static void main(String[] args) throws InterruptedException{
		
		Thread t1 = new Thread(new Task(1));
		Thread t2 = new Thread(new Task(2));
		Thread t3 = new Thread(new Task(3));
		Thread t4 = new Thread(new Task(4));
		Thread t5 = new Thread(new Task(5));
		
		t1.start();
		t1.join();
		t2.start();
		t2.join();
		t3.start();
		t3.join();
		t4.start();
		t4.join();
		t5.start();
		
		
		ExecutorService service = Executors.newSingleThreadExecutor();
		for(int i=1; i<= 5; i++){
			int num = i;
			//service.submit(() -> System.out.println(num));
		}
		
		for(int i=0; i< 5; i++){
			Task task = new Task(i);
			service.submit(task);
		}
	}
	static class Task implements Runnable{
		int index;
		Task(int index){
			this.index = index;
		}
		@Override
		public void run() {
			
			for(int i = 0; i < 100; i++) {
				System.out.print("Thread" + index + ": " + i + " ");
			}
			System.out.println();
		}	
	}
}






