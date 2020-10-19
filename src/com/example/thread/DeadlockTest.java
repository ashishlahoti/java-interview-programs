package com.example.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockTest {
	
	private Lock lockA = new ReentrantLock();
	private Lock lockB = new ReentrantLock();
	private CountDownLatch latch = new CountDownLatch(2);

	public static void main(String[] args){
		DeadlockTest test = new DeadlockTest();
		test.execute();
	}
	
	private void execute() {
		new Thread(this::processThis).start();
		new Thread(this::processThat).start();
	}
	
	private void processThis() {
		lockA.lock();
		System.out.println(Thread.currentThread().getName() + " processing resource A");
		
		lockB.lock();
		System.out.println(Thread.currentThread().getName() + " processing resource B");
		
		
		lockA.unlock();
		lockB.unlock();
	}
	
	private void processThat() {
		lockB.lock();
		System.out.println(Thread.currentThread().getName() + " processing resource B");
		
		lockA.lock();
		System.out.println(Thread.currentThread().getName() + " processing resource A");
		
		
		lockA.unlock();
		lockB.unlock();
	}
	
	private static void detectedDeadLock() {
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		long[] threadIds = threadBean.findDeadlockedThreads();
		boolean deadLock = threadIds != null && threadIds.length > 0;
		System.out.println("DeadLock found: " + deadLock);
	}
}
