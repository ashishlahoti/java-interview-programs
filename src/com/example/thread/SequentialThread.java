package com.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SequentialThread {

	public static void main(String[] args){
		ExecutorService service = Executors.newSingleThreadExecutor();
		for(int i=0; i< 100; i++){
			Task task = new Task(i);
			service.submit(task);
		}
	}
}

class Task implements Runnable{
	int index;
	Task(int index){
		this.index = index;
	}
	@Override
	public void run() {
		
		for(int i = 0; i < 50; i++) {
			System.out.print(index + "_" + i + " ");
		}
		System.out.println();
	}
	
}




