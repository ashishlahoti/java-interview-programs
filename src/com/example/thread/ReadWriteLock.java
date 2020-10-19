package com.example.thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
	private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
	
	private void readResource() {
		readLock.lock();
		// read the resource
		readLock.unlock();
	}
	
	private void writeResource() {
		writeLock.lock();
		// write the resource
		writeLock.unlock();
	}
	
	public static void main(String[] args) {
		ReadWriteLock obj = new ReadWriteLock();
		Thread t1 = new Thread(() -> obj.readResource()); t1.start();
		Thread t2 = new Thread(() -> obj.readResource()); t2.start();
		Thread t3 = new Thread(() -> obj.writeResource()); t3.start();
		Thread t4 = new Thread(() -> obj.writeResource()); t4.start();
		Thread t5 = new Thread(() -> obj.readResource()); t5.start();
		
		//t1,t2 can read at the same time
		//t3,t4 can not write when t1,t2 are reading
		//t4 can not write when t3 is writing
		//t5 can not read when t3 is writing
	}	
}
