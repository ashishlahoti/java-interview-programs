package com.help4j.core.thread;

public class DeadlockTest {

	public static void main(String[] args){
		LockObject lock1 = new LockObject("lock1");
		LockObject lock2 = new LockObject("lock2");
		Thread threadA = new Thread(new LockingThread(lock1, lock2));
		Thread threadB = new Thread(new LockingThread(lock2, lock1));
		threadA.start();
		threadB.start();
	}
}

class LockingThread implements Runnable{
	LockObject lock1 = null;
	LockObject lock2 = null;
	LockingThread(LockObject lock1, LockObject lock2){
		this.lock1 = lock1;
		this.lock2 = lock2;
	}
	
	@Override
	public void run() {
		System.out.println("Waiting " + lock1.name + " by Thread " + Thread.currentThread().getName());
		synchronized (lock1) {
			System.out.println("Aquired " + lock1.name + " by Thread " + Thread.currentThread().getName());
			System.out.println("Waiting " + lock2.name + " by Thread " + Thread.currentThread().getName());
			synchronized (lock2) {
				System.out.println("Aquired " + lock2.name + " by Thread " + Thread.currentThread().getName());
			}
		}
	}
	
}

class LockObject {
	String name;
	LockObject(String name){
		this.name = name;
	}
	@Override
	public String toString() {
		return this.name;
	}
}