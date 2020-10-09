package com.example.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IdealThreadPoolSize {

	public static void main(String[] args) {
		// get count of available cores
		int coreCount = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(coreCount);

		// submit the tasks for execution
		for (int i = 0; i < 100; i++) {
			service.execute(new CpuIntestiveTask());
		}
	}

	static class CpuIntestiveTask implements Runnable {
		public void run() {
			// some CPU intensive operations
		}
	}
}
