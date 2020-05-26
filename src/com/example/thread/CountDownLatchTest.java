package com.example.thread;

import java.util.concurrent.CountDownLatch;
/**
 * CountDownLatch is useful if you want to start main processing thread once its dependency is completed
 * @author aklahoti
 *
 */
public class CountDownLatchTest {

	public static void main(String[] args) throws InterruptedException{
		CountDownLatch latch = new CountDownLatch(3);
		Thread cacheService = new Thread(new CountDownLatchRunnable("Cache Service", latch));
		Thread alertService = new Thread(new CountDownLatchRunnable("Alert Service", latch));
		Thread validationService = new Thread(new CountDownLatchRunnable("Validation Service", latch));
		cacheService.start();
		alertService.start();
		validationService.start();
		latch.await(); //wait here till latch count reaches 0 then only process next line of code.
		System.out.println("All Services are up and running. Main Thread started processing...");
		
	}
}

class CountDownLatchRunnable implements Runnable{
	
	private String name;
	private CountDownLatch latch;
	
	CountDownLatchRunnable(String name, CountDownLatch latch){
		this.name = name;
		this.latch = latch;
	}

	@Override
	public void run() {
		System.out.println(name + " is up");
		latch.countDown();
		
	}
	
}
