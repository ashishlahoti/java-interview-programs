package com.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecuterTest {

	public static void main(String[] args) {
		//ExecutorService service = Executors.newSingleThreadExecutor(new ExecutorThreadFactory());
		ExecutorService service = Executors.newFixedThreadPool(4, new ExecutorThreadFactory("Executor_Test_Thread_"));
		for(int i=1; i<10; i++){
			service.execute(new ExecuterTestTask());
		}
		
	}
}

class ExecuterTestTask implements Runnable {

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName());
		
	}
}

class ExecutorThreadFactory implements ThreadFactory {
	
	AtomicInteger counter = new AtomicInteger();
	
	String threadNamePrefix;
	
	ExecutorThreadFactory(String prefix){
		this.threadNamePrefix = prefix;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, threadNamePrefix + counter.incrementAndGet());
		thread.setDaemon(false);
		return thread;
	}
	
}
