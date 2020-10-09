package com.example.thread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RunnableVsCallable {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService service = Executors.newSingleThreadExecutor();
		
		// execute runnable task which return nothing
		service.execute(new RunnableTask());
		
		// submit callable task which return Future object
		Future<Integer> future = service.submit(new CallableTask());
		
		// get result from future object which is blocking operation
		Integer result = future.get();
		
		// shutdown the thread gracefully
		service.shutdown();
	}

	static class RunnableTask implements Runnable {

		@Override
		public void run() {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class CallableTask implements Callable<Integer> {
		
		@Override
		public Integer call() throws InterruptedException {
			Thread.sleep(1000);
			return new Random().nextInt();
		}
	}
}
