package com.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecuterTest {

	public static void main(String[] args) {
		// for fixed number of threads
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

		// for a pool of single thread
		ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

		// for lot of short lived tasks
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

		// for scheduling of tasks
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

		// task to run after 10 second delay
		scheduledThreadPool.schedule(new Task(), 10, TimeUnit.SECONDS);

		// task to run after initial delay of 15 seconds at every 10 second interval
		scheduledThreadPool.scheduleAtFixedRate(new Task(), 15, 10, TimeUnit.SECONDS);

		// task to run after initial delay of 15 second at every 10 second after previous task completes
		scheduledThreadPool.scheduleWithFixedDelay(new Task(), 15, 10, TimeUnit.SECONDS);
		
		//ExecutorService service = Executors.newSingleThreadExecutor(new ExecutorThreadFactory());
		ExecutorService service = Executors.newFixedThreadPool(5, new ExecutorThreadFactory("Test_Thread_"));
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
