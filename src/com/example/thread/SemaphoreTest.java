package com.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Semaphore is used when you want to limit maximum number of concurrent calls
 * to a particular resource in multi-threaded environment.
 * 
 * @author aklahoti
 */
public class SemaphoreTest {

	public static void main(String[] args) throws InterruptedException {
		
		// ThreadPool of 50 threads
		ExecutorService service = Executors.newFixedThreadPool(50);
		
		// Semaphore to rum max 3 concurrent calls
		Semaphore semaphore = new Semaphore(3);
		
		// Execute 1000 tasks (initiate 1000 threads) to test
		IntStream.of(1000).forEach(i -> service.execute(new Task(semaphore)));
		
		// Shutdown the ExecutorService gracefully
		service.shutdown();
		service.awaitTermination(1, TimeUnit.MINUTES);
	}

	static class Task implements Runnable {

		private Semaphore semaphore;

		Task(Semaphore semaphore) {
			this.semaphore = semaphore;
		}

		@Override
		public void run() {
			
			// Start code -> can be executed by 50 threads concurrently 
			// since thread pool size is 50

			try {
			// Only 3 threads can acquire lock at a time since semaphore count is 3
			// Other threads wait here.
			semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Slow Code -> Heavy operations like IO 
			// can be executed by only 3 threads

			// Release the lock for other threads to come in Slow code
			semaphore.release();

			// End code -> can be executed by 50 threads concurrently 
			// since thread pool size is 50
		}
	}
}