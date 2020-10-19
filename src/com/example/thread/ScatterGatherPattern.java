package com.example.thread;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScatterGatherPattern {

	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		ScatterGatherPattern scatterGather = new ScatterGatherPattern();
		scatterGather.getPrices(1);
	}

	private Set<Double> getPrices(int productId) throws InterruptedException, ExecutionException, TimeoutException {
		Set<Double> prices = Collections.synchronizedSet(new HashSet<>());
		CompletableFuture<Void> task1 = CompletableFuture.runAsync(new Task("amazon.com", productId, prices));
		CompletableFuture<Void> task2 = CompletableFuture.runAsync(new Task("ebay.com", productId, prices));
		CompletableFuture<Void> task3 = CompletableFuture.runAsync(new Task("wallmart.com", productId, prices));
		CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1, task2, task3);

		// wait for all the taks to complete, but max for 3 seconds
		allTasks.get(3, TimeUnit.SECONDS);

		return prices;
	}

	private static class Task implements Runnable {
		private String url;
		private int productId;
		private Set<Double> set;

		Task(String url, int productId, Set<Double> set) {
			this.url = url;
			this.productId = productId;
			this.set = set;
		}

		@Override
		public void run() {
			double price = 0;
			// make http call (url, productId) to get price
			set.add(price);
		}
	}
}
