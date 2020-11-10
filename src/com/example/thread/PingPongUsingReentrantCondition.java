package com.example.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PingPongUsingReentrantCondition {

	private static ReentrantLock lock = new ReentrantLock();
	private Condition conditionMet = lock.newCondition();

	public static void main(String[] args) {
		PingPongUsingReentrantCondition pingPong = new PingPongUsingReentrantCondition();
		int times = 5;
		System.out.println("N = " + times);
		Thread t1 = new Thread(() -> pingPong.pingpong("Ping!", times));
		Thread t2 = new Thread(() -> pingPong.pingpong("Pong!", times));
		t1.start();
		t2.start();
	}
	
	public void pingpong(String s, int times) {
		int counter = 1;
		while(counter<=times) {				
			run(s, counter);
			counter = counter+1;
		}
	}

	public void run(String s, int counter) {
		lock.lock();
		try {
			conditionMet.await(2, TimeUnit.SECONDS);
			System.out.println(s + "(" + counter + ")");
			conditionMet.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
