package com.example.thread;

import java.util.concurrent.CountDownLatch;

public class StartThreadsAtSameTime {

	public static void main(String[] args) throws InterruptedException {
		// Countdown = 5
		CountDownLatch latch = new CountDownLatch(5);

		// Initialize and start 5 threads passing the same latch object
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new Task(latch));
			t.start();
		}

		System.out.println("All threads wait until Countdown = 0");

		// Decrement the Countdown 5 times
		for (int i = 0; i < 5; i++) {
			latch.countDown();
		}

		// All threads start printing at this point
	}

	static class Task implements Runnable {

		private CountDownLatch latch;

		Task(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				// thread wait for Countdown = 0
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + " is running");
		}
	}
}
