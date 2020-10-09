package com.example.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	public static void main(String[] args) {
		CyclicBarrier barrier = new CyclicBarrier(3);
		Thread t1 = new Thread(new Task(barrier));
		Thread t2 = new Thread(new Task(barrier));
		Thread t3 = new Thread(new Task(barrier));
		t1.start();
		t2.start();
		t3.start();

	}

	static class Task implements Runnable {

		private CyclicBarrier barrier;

		Task(CyclicBarrier barrier) { this.barrier = barrier;}

		@Override
		public void run() {
			while(true) {
				String threadName = Thread.currentThread().getName();
				System.out.println(threadName + " running towards barrier");
				
				try {
					barrier.await();  // wait for other threads to reach at barrier point
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				
				System.out.println(threadName + " crossed barrier");				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}		
		}
	}
}