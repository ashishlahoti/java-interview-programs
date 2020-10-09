package com.example.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A Task is running in a separate thread. 
 * Stop the task if it exceeds 10 minutes
 * 
 * @author aklahoti
 */
public class StopThreadAfterTimeout {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// usingThreadInterrupt();
		// usingThreadPoolShutdownNow();
		// usingFutureCancel();
		usingVolatileBooleanFlag();
	}

	private static void usingThreadInterrupt() throws InterruptedException {
		// 1. create a thread
		Thread t1 = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				// next step
			}
		});

		// 2. timeout after 10 minutes
		Thread.sleep(10 * 60 * 1000);

		// 3. stop the thread
		t1.interrupt();
	}

	private static void usingThreadPoolShutdownNow() throws InterruptedException {
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		// 1. create a thread
		threadPool.submit(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				// next step
			}
		});

		// 2. timeout after 10 minutes
		Thread.sleep(10 * 60 * 1000);

		// 3. stop the thread
		threadPool.shutdownNow(); // internally call thread interrupt
	}

	private static void usingFutureCancel() throws InterruptedException, ExecutionException {
		ExecutorService service = Executors.newFixedThreadPool(2);

		// 1. submit new callable task which return future object
		Future<?> future = service.submit(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				// next step
			}
		});

		// 2. wait for 10 minutes to get response
		try {
			future.get(10, TimeUnit.MINUTES);
		} catch (TimeoutException e) {

			// 3. stop the thread
			future.cancel(true); // internally call thread interrupt
		}
	}

	private static void usingVolatileBooleanFlag() throws InterruptedException {
		final class Task extends Thread {
			private volatile boolean flag = true;

			public void stopRunning() {
				flag = false;
			}

			@Override
			public void run() {
				while (flag) {
					System.out.println("I am running....");
				}
				System.out.println("Stopped Running....");
			}
		}
		
		Task task = new Task();
		
		task.start();
		Thread.sleep(2 * 1000);		
		task.stopRunning();
	}
}
