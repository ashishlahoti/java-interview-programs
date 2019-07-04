package com.help4j.core.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	public static void main(String[] args){
		CyclicBarrier barrier = new CyclicBarrier(2);
		Thread t1 = new Thread(new CyclicBarrierRunnable("Thread 1", barrier));
		Thread t2 = new Thread(new CyclicBarrierRunnable("Thread 2", barrier));
		
		t1.start();
		t2.start();
		
	}
}

class CyclicBarrierRunnable implements Runnable{

	private String name;
	private CyclicBarrier barrier;
	
	CyclicBarrierRunnable(String name, CyclicBarrier barrier){
		this.name = name;
		this.barrier = barrier;
	}
	
	@Override
	public void run() {
		System.out.println(name + " running towards barrier");
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println(name + " crossed barrier");
		
	}
	
}
